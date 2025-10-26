<template>
  <div class="worker-page">
    <Loader v-if="loading" />

    <WorkerInfo
        v-else-if="worker"
        :photo="worker.photo"
        :firstName="worker.firstName"
        :name="worker.lastName"
        :patronymic="worker.patronymic || '-'"
        :position="worker.position"
        :salary="worker.salary"
        :phone="worker.phone || '-'"
        :email="worker.email || '-'"
        :status="worker.status || '-'"
    />

    <p v-else class="error">Сотрудник не найден</p>
  </div>
</template>

<script setup>
import { computed } from "vue";
import { useRoute } from "vue-router";
import Loader from "@/components/ui/Loader.vue";
import WorkerInfo from "@/sections/WorkerInfo.vue";
import {useWorkers} from "@/composables/useWorkers.js";

const route = useRoute();
const workerId = Number(route.params.id);

const { workers, loading } = useWorkers();

const worker = computed(() => workers.value.find(u => u.id === workerId));
</script>

<style scoped lang="scss">
.worker-page {
  min-height: 60vh;
}
</style>
