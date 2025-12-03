import { ref, onMounted } from "vue";
import { getChickens } from "@/api/chickens.js";
import { getWorkers } from "@/api/workers.js";
import { getBreeds } from "@/api/breeds.js";
import { getWorkshops } from "@/api/workshops.js";
import { getRows } from "@/api/rows.js";
import { getCages } from "@/api/cages.js";
import { getLastEggProduction } from "@/api/eggProductionMonth.js";

export default function useFarmStatistics() {
  const statistics = ref({
    chickensCount: 0,
    workersCount: 0,
    breedsCount: 0,
    workshopsCount: 0,
    rowsCount: 0,
    cagesCount: 0,
    avgEggsPerMonth: 0,
    avgProductivity: 0,

    avgEggsPerChicken: 0,
    totalEggsThisMonth: 0,
    avgChickenAge: 0
  });

  const loading = ref(true);
  const error = ref(null);

  const fetchAllStatistics = async () => {
    loading.value = true;
    error.value = null;
    
    let chickensData = [];
    let workshopsData = [];

    try {
      // Запускаем все базовые запросы параллельно, но обновляем UI по мере получения
      
      // 1. Курицы
      getChickens()
        .then((chickens) => {
          chickensData = chickens || [];
          statistics.value = {
            ...statistics.value,
            chickensCount: chickensData.length,
            avgProductivity: calculateAverageProductivity(chickensData),
            avgChickenAge: calculateAverageAge(chickensData)
          };
        })
        .catch((err) => {
          console.error("Ошибка при получении кур:", err);
        });

      // 2. Сотрудники
      getWorkers()
        .then((workers) => {
          statistics.value = {
            ...statistics.value,
            workersCount: workers?.length || 0
          };
        })
        .catch((err) => {
          console.error("Ошибка при получении сотрудников:", err);
        });

      // 3. Породы
      getBreeds()
        .then((breeds) => {
          statistics.value = {
            ...statistics.value,
            breedsCount: breeds?.length || 0
          };
        })
        .catch((err) => {
          console.error("Ошибка при получении пород:", err);
        });

      // 4. Цехи (после получения запускаем зависимые запросы)
      getWorkshops()
        .then(async (workshops) => {
          workshopsData = workshops || [];
          statistics.value = {
            ...statistics.value,
            workshopsCount: workshopsData.length
          };

          // После получения цехов запускаем запросы для рядов и клеток
          // 5. Ряды
          getRowsData(workshopsData)
            .then((rowsCount) => {
              statistics.value = {
                ...statistics.value,
                rowsCount: rowsCount || 0
              };
            })
            .catch((err) => {
              console.error("Ошибка при получении рядов:", err);
            });

          // 6. Клетки
          getCagesData(workshopsData)
            .then((cagesCount) => {
              statistics.value = {
                ...statistics.value,
                cagesCount: cagesCount || 0
              };
            })
            .catch((err) => {
              console.error("Ошибка при получении клеток:", err);
            });
        })
        .catch((err) => {
          console.error("Ошибка при получении цехов:", err);
        });

      // 7. Статистика яиц (запускаем после получения кур)
      // Ждем немного, чтобы chickensData успел обновиться, или используем отдельный запрос
      const chickensForEggs = await getChickens().catch(() => []);
      if (chickensForEggs && chickensForEggs.length > 0) {
        getEggsStatistics(chickensForEggs)
          .then((eggsData) => {
            statistics.value = {
              ...statistics.value,
              avgEggsPerMonth: eggsData.avgEggsPerMonth || 0,
              avgEggsPerChicken: eggsData.avgEggsPerChicken || 0,
              totalEggsThisMonth: eggsData.totalEggsThisMonth || 0
            };
          })
          .catch((err) => {
            console.error("Ошибка при получении статистики яиц:", err);
          });
      }

      // Устанавливаем monthlyProfit сразу
      statistics.value = {
        ...statistics.value,
        monthlyProfit: "28.9KK"
      };

      // Даем время на загрузку всех данных перед снятием флага loading
      // Используем Promise.allSettled для отслеживания всех запросов
      await Promise.allSettled([
        getChickens(),
        getWorkers(),
        getBreeds(),
        getWorkshops()
      ]);

      // Небольшая задержка для завершения зависимых запросов
      setTimeout(() => {
        loading.value = false;
      }, 500);

    } catch (err) {
      console.error("Ошибка при загрузке статистики:", err);
      error.value = "Не удалось загрузить статистику";
      loading.value = false;
    }
  };

  const getEggsStatistics = async (chickens = null) => {
    try {
      // Если данные не переданы, получаем их (для обратной совместимости)
      const chickensData = chickens || await getChickens();
      
      if (!chickensData || chickensData.length === 0) {
        return {
          avgEggsPerMonth: 0,
          avgEggsPerChicken: 0,
          totalEggsThisMonth: 0
        };
      }

      // Параллельно получаем яйценоскость для всех кур
      const eggsPromises = chickensData.map(async (chicken) => {
        try {
          const eggProduction = await getLastEggProduction(chicken.id);
          return eggProduction || 0;
        } catch (err) {
          console.error(`Ошибка при получении яйценоскости для курицы ${chicken.id}:`, err);
          return 0;
        }
      });

      const eggsData = await Promise.allSettled(eggsPromises);

      const successfulEggsData = eggsData
        .filter(result => result.status === 'fulfilled' && result.value !== null && result.value !== undefined)
        .map(result => result.value);

      if (successfulEggsData.length === 0) {
        return {
          avgEggsPerMonth: 0,
          avgEggsPerChicken: 0,
          totalEggsThisMonth: 0
        };
      }

      const totalEggs = successfulEggsData.reduce((sum, eggs) => sum + eggs, 0);
      const avgEggsPerChicken = Math.round(totalEggs / successfulEggsData.length);

      const totalEggsThisMonth = totalEggs;

      return {
        avgEggsPerMonth: totalEggs,
        avgEggsPerChicken,
        totalEggsThisMonth
      };

    } catch (err) {
      console.error("Ошибка при расчете статистики яиц:", err);
      return {
        avgEggsPerMonth: 0,
        avgEggsPerChicken: 0,
        totalEggsThisMonth: 0
      };
    }
  };

  const calculateAverageAge = (chickens) => {
    if (!chickens || chickens.length === 0) return 0;

    let totalAgeInMonths = 0;
    let chickensWithAge = 0;
    const currentDate = new Date();

    chickens.forEach(chicken => {
      if (chicken.birthDate) {
        try {
          const birthDate = new Date(chicken.birthDate);

          if (isNaN(birthDate.getTime())) {
            console.error(`Некорректная дата рождения у курицы ${chicken.id}: ${chicken.birthDate}`);
            return;
          }

          const yearsDiff = currentDate.getFullYear() - birthDate.getFullYear();
          const monthsDiff = currentDate.getMonth() - birthDate.getMonth();
          const daysDiff = currentDate.getDate() - birthDate.getDate();

          let ageInMonths = yearsDiff * 12 + monthsDiff;

          if (daysDiff < 0) {
            ageInMonths--;
          }

          ageInMonths = Math.max(0, ageInMonths);

          totalAgeInMonths += ageInMonths;
          chickensWithAge++;

        } catch (err) {
          console.error(`Ошибка при расчете возраста курицы ${chicken.id}:`, err);
        }
      }
    });

    if (chickensWithAge === 0) return 0;

    const avgAge = totalAgeInMonths / chickensWithAge;
    return Math.round(avgAge);
  };

  const getRowsData = async (workshops = null) => {
    try {
      // Если данные не переданы, получаем их (для обратной совместимости)
      const workshopsData = workshops || await getWorkshops();
      
      if (!workshopsData || workshopsData.length === 0) {
        return 0;
      }

      // Параллельно получаем ряды для всех цехов
      const rowsPromises = workshopsData.map(async (workshop) => {
        try {
          const rows = await getRows(workshop.id);
          return rows.length;
        } catch (err) {
          console.error(`Ошибка при получении рядов для цеха ${workshop.id}:`, err);
          return 0;
        }
      });

      const rowsResults = await Promise.allSettled(rowsPromises);
      
      const totalRows = rowsResults.reduce((sum, result) => {
        if (result.status === 'fulfilled') {
          return sum + result.value;
        }
        return sum;
      }, 0);

      return totalRows;
    } catch (err) {
      console.error("Ошибка при получении рядов:", err);
      return 0;
    }
  };

  const getCagesData = async (workshops = null) => {
    try {
      // Если данные не переданы, получаем их (для обратной совместимости)
      const workshopsData = workshops || await getWorkshops();
      
      if (!workshopsData || workshopsData.length === 0) {
        return 0;
      }

      // Параллельно получаем ряды для всех цехов
      const rowsPromises = workshopsData.map(async (workshop) => {
        try {
          const rows = await getRows(workshop.id);
          return rows;
        } catch (err) {
          console.error(`Ошибка при получении рядов для цеха ${workshop.id}:`, err);
          return [];
        }
      });

      const rowsResults = await Promise.allSettled(rowsPromises);
      
      // Собираем все ряды в один массив
      const allRows = [];
      rowsResults.forEach((result) => {
        if (result.status === 'fulfilled' && Array.isArray(result.value)) {
          allRows.push(...result.value);
        }
      });

      if (allRows.length === 0) {
        return 0;
      }

      // Параллельно получаем клетки для всех рядов
      const cagesPromises = allRows.map(async (row) => {
        try {
          const cages = await getCages(row.id);
          return cages.length;
        } catch (err) {
          console.error(`Ошибка при получении клеток для ряда ${row.id}:`, err);
          return 0;
        }
      });

      const cagesResults = await Promise.allSettled(cagesPromises);
      
      const totalCages = cagesResults.reduce((sum, result) => {
        if (result.status === 'fulfilled') {
          return sum + result.value;
        }
        return sum;
      }, 0);

      return totalCages;
    } catch (err) {
      console.error("Ошибка при получении клеток:", err);
      return 0;
    }
  };

  const calculateAverageProductivity = (chickens) => {
    if (!chickens || chickens.length === 0) return 0;

    const totalProductivity = chickens.reduce((sum, chicken) => {
      return sum + (chicken.eggs || 0);
    }, 0);

    return Math.round(totalProductivity / chickens.length);
  };

  onMounted(fetchAllStatistics);

  return {
    statistics,
    loading,
    error,
    refreshStatistics: fetchAllStatistics
  };
}