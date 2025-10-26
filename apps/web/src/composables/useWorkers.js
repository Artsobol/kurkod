import {onMounted, ref} from "vue";
import {getWorkerContract, getWorkers} from "@/api/workers.js";

export function useWorkers() {
  const workers = ref([]);
  const loading = ref(true);

  onMounted(async () => {
    try {
      const baseWorkers = await getWorkers();
      workers.value = await Promise.all(
        baseWorkers.map(async (worker) => {
          const contract = await getWorkerContract(worker.id);
          return {
            ...worker,
            position: contract?.position || "-",
            salary: contract?.salary || "-",
          };
        })
      );
    } catch (e) {
      console.error("Ошибка при получении данных:", e);
    } finally {
      loading.value = false;
    }
  });

  return { workers, loading };
}