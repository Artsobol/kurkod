import {onMounted, ref} from "vue";
import {getChickens} from "@/api/chickens.js";
import {getAgeFromDate} from "@/utils/age.js";

export function useChickens() {
  const chickens = ref([]);
  const loading = ref(true);

  const fetchChickens = async () => {
    loading.value = true;
    try {
      const baseChickens = await getChickens();
      chickens.value = baseChickens.map(chicken => ({
        ...chicken,
        age: getAgeFromDate(chicken.birthDate),
      }));
    } catch (e) {
      console.error("Ошибка при получении данных:", e);
    } finally {
      loading.value = false;
    }
  };

  onMounted(fetchChickens);

  return {chickens, loading, fetchChickens};
}
