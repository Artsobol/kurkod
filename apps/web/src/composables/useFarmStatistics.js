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
    try {
      const [
        chickens,
        workers,
        breeds,
        workshops,
        allRows,
        allCages,
        eggsData
      ] = await Promise.allSettled([
        getChickens(),
        getWorkers(),
        getBreeds(),
        getWorkshops(),
        getRowsData(),
        getCagesData(),
        getEggsStatistics()
      ]);

      const chickensData = chickens.status === 'fulfilled' ? chickens.value : [];

      statistics.value = {
        chickensCount: chickensData.length,
        workersCount: workers.status === 'fulfilled' ? workers.value.length : 0,
        breedsCount: breeds.status === 'fulfilled' ? breeds.value.length : 0,
        workshopsCount: workshops.status === 'fulfilled' ? workshops.value.length : 0,
        rowsCount: allRows.status === 'fulfilled' ? allRows.value : 0,
        cagesCount: allCages.status === 'fulfilled' ? allCages.value : 0,
        avgEggsPerMonth: eggsData.status === 'fulfilled' ? eggsData.value.avgEggsPerMonth : 0,
        avgProductivity: calculateAverageProductivity(chickensData),
        monthlyProfit: "28.9KK",

        avgEggsPerChicken: eggsData.status === 'fulfilled' ? eggsData.value.avgEggsPerChicken : 0,
        totalEggsThisMonth: eggsData.status === 'fulfilled' ? eggsData.value.totalEggsThisMonth : 0,
        avgChickenAge: calculateAverageAge(chickensData)
      };

    } catch (err) {
      console.error("Ошибка при загрузке статистики:", err);
      error.value = "Не удалось загрузить статистику";
    } finally {
      loading.value = false;
    }
  };

  const getEggsStatistics = async () => {
    try {
      const chickens = await getChickens();
      if (!chickens || chickens.length === 0) {
        return {
          avgEggsPerMonth: 0,
          avgEggsPerChicken: 0,
          totalEggsThisMonth: 0
        };
      }

      const eggsPromises = chickens.map(async (chicken) => {
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

  const getRowsData = async () => {
    try {
      const workshops = await getWorkshops();
      let totalRows = 0;

      for (const workshop of workshops) {
        try {
          const rows = await getRows(workshop.id);
          totalRows += rows.length;
        } catch (err) {
          console.error(`Ошибка при получении рядов для цеха ${workshop.id}:`, err);
        }
      }

      return totalRows;
    } catch (err) {
      console.error("Ошибка при получении рядов:", err);
      return 0;
    }
  };

  const getCagesData = async () => {
    try {
      const workshops = await getWorkshops();
      let totalCages = 0;

      for (const workshop of workshops) {
        try {
          const rows = await getRows(workshop.id);

          for (const row of rows) {
            try {
              const cages = await getCages(row.id);
              totalCages += cages.length;
            } catch (err) {
              console.error(`Ошибка при получении клеток для ряда ${row.id}:`, err);
            }
          }
        } catch (err) {
          console.error(`Ошибка при получении рядов для цеха ${workshop.id}:`, err);
        }
      }

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