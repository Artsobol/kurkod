<template>
  <div class="worker-page">
    <Loader v-if="loading" />

    <ChickenInfo
        v-else-if="chicken"
        :name="chicken.name"
        :weight="chicken.weight"
        :birth-date="chicken.birthDate"
        :age="age"
    />

    <p v-else class="error">Сотрудник не найден</p>
  </div>
</template>

<script setup>
import { computed } from "vue";
import { useRoute } from "vue-router";
import Loader from "@/components/ui/Loader.vue";
import ChickenInfo from "@/sections/ChickenInfo.vue";
import { useChickens } from "@/composables/useChickens.js";
import { getAgeFromDate } from "@/utils/age.js";
import { formatDate } from "@/utils/formatDate.js";

const route = useRoute();
const chickenId = Number(route.params.id);

const { chickens, loading } = useChickens();

const chicken = computed(() => chickens.value.find(c => c.id === chickenId));

const age = computed(() => {
  if (!chicken.value?.birthDate) return "";
  return getAgeFromDate(chicken.value.birthDate);
});

const formattedBirthDate = computed(() => {
  if (!chicken.value?.birthDate) return "";
  return formatDate(chicken.value.birthDate);
});
</script>

<style scoped lang="scss">
.worker-page {
  min-height: 60vh;
}
</style>
