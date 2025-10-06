<template>
  <div class="employees">
    <h2 class="employees__title">Сотрудники</h2>
    <div class="page-block">
      <TableTemplate
          title="Сотрудники"
          :bodyItems="['Имя', 'Должность', 'Статус']"
          :headersItem="['', 'Имя', 'Должность', 'Статус']"
          stylesPack="table-employees" />
    </div>
  </div>
</template>

<script setup>
import InfoBlock from "@/components/ui/InfoBlock.vue";
import InfoGraph from "@/components/ui/InfoGraph.vue";
import TableTemplate from "@/components/tables/TableTemplate.vue";
</script>

<style lang="scss" scoped>
.page-block {
  background: var(--color-white);
  border-radius: 6px;
  box-shadow: 0 4px 4px 0 rgba(61, 55, 136, 0.25);
  padding: 12px 19px 17px 17px;
}

.employees {
  &__title {
    padding-bottom: 12px;
    font-size: 18px;
  }
}
</style>