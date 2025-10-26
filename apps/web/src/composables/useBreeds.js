import {onMounted, ref} from "vue";
import {getBreeds} from "@/api/breeds.js";

export default function useBreeds() {
  const breeds = ref([]);
  const loading = ref(true);

  onMounted(async () => {
    try {
      breeds.value = await getBreeds();
    } catch (e) {
      console.error("Ошибка при получении данных:", e);
    } finally {
      loading.value = false;
    }
  });

  return { breeds, loading };
}