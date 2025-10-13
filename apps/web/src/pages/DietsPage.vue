<template>
  <div class="diets">
    <div class="diets__header">
      <h2 class="diets__title">Диеты</h2>
    </div>
    <div class="diets__actions">
      <Input
          class="diets__input"
          labelInput="Найти диету"
      />
      <Button
          label="Добавить диету"
          mode="violet"
          location="page-action"
      />
      <Button
          label="Удалить диету"
          mode="violet"
          location="page-action"
      />
    </div>
    <TableDraft :headersItem="[
    { key: 'photo', label: 'Фото' },
    { key: 'name', label: 'Имя' },
    { key: 'position', label: 'Должность' },
    { key: 'salary', label: 'Зарплата' },
    { key: 'status', label: 'Статус' } ]"
        :bodyItems="users"
    />
  </div>
</template>
<script setup>
import Input from "@/components/ui/Input.vue";
import Button from "@/components/ui/Button.vue";
import TableDraft from "@/components/tables/TableDraft.vue";
import employees_data from "@/constants/EMPLOYEES_DATA.json";

const users = employees_data;

</script>
<style lang="scss" scoped>
.diets {
  &__header {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }

  &__title {
    margin-bottom: 16px;
  }

  &__actions {
    margin-bottom: 16px;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    gap: 32px;
  }
}
</style>