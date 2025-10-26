import {onMounted, ref} from "vue";
import {getDiets} from "@/api/diets.js";

export default function useDiets() {
  const diets = ref([]);
  const loading = ref(true);

  onMounted(async () => {
    try {
      diets.value = await getDiets();
    } catch (e) {
      console.error("Ошибка при получении данных:", e);
    } finally {
      loading.value = false;
    }
  });

  return { diets, loading };
}