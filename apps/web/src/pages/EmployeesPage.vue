<template>
  <div class="employees">
    <div class="employees__actions">
      <Input
          class="employees__input"
          label-input="Найти сотрудника"
      />
      <Button
          label="Добавить сотрудника"
          mode="violet"
          location="page-action"
      />
      <Button
          label="Удалить сотрудника"
          mode="violet"
          location="page-action"
      />
      <Button
          style="margin-left: auto;"
          href="/cells"
          class="table-template__button"
          label="Закрепленные клетки"
          icon-name="arrow-right"
          :icon-width="28"
          :icon-height="28"
      />
    </div>

    <TableTemplate :headersItem="[
          { key: 'photo', label: 'Фото' },
          { key: 'name', label: 'ФИО' },
          { key: 'position', label: 'Должность' },
          { key: 'salary', label: 'Зарплата' },
          { key: 'status', label: 'Статус' } ]"
                :bodyItems="users"
                :height-size="100"
    />
  </div>
</template>

<script setup>
import Input from "@/components/ui/Input.vue";
import Button from "@/components/ui/Button.vue";
import TableTemplate from "@/components/tables/TableTemplate.vue";
import employees_data from "@/constants/EMPLOYEES_DATA.json";

const users = employees_data;
</script>

<style lang="scss" scoped>
.employees {
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