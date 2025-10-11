<template>
  <div class="employees">
    <div class="employees__header">
      <h2 class="employees__title">Сотрудники</h2>
      <Button
          href="/cells"
          class="table-template__button"
          label="Закрепленные клетки"
          icon-name="arrow-right"
          :icon-width="28"
          :icon-height="28"
      />
    </div>
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
    </div>

    <div class="page-block">
      <TableTemplate
          title="Сотрудники"
          :body-items="['Имя', 'Должность', 'Статус']"
          :headers-item="['', 'Имя', 'Должность', 'Статус']"
          styles-pack="table-employees"/>
    </div>
  </div>
</template>

<script setup>
import TableTemplate from "@/components/tables/TableTemplate.vue";
import Input from "@/components/ui/Input.vue";
import Button from "@/components/ui/Button.vue";
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