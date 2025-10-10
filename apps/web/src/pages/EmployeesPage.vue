<template>
  <div class="employees">
    <div class="employees__header">
      <h2 class="employees__title">Сотрудники</h2>
      <Button
          href="/cells"
          class="table-template__button"
          specific-button="arrow-right"
          label="Закрепленные клетки"
      />
    </div>
    <div class="employees__actions">
      <Input
          class="employees__input"
          labelInput="Найти сотрудника"
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
          :bodyItems="['Имя', 'Должность', 'Статус']"
          :headersItem="['', 'Имя', 'Должность', 'Статус']"
          stylesPack="table-employees"/>
    </div>
  </div>
</template>

<script setup>
import InfoBlock from "@/components/ui/InfoBlock.vue";
import InfoGraph from "@/components/ui/InfoGraph.vue";
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

  &__extra-button {
    display: flex;
    align-items: center;
    gap: 16px;
    &__title {
      font-weight: 700;
      color: var(--color);
    }
    &__icon {
      width: 28px;
      height: 28px;
    }
  }
}
</style>