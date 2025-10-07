<template>
  <div class="employees">
    <div class="employees__header">
      <h2 class="employees__title">Сотрудники</h2>
      <a class="employees__extra-button" href="#">
      <span class="employees__extra-button__title">
        Закрепленные клетки
      </span>
        <img
            class="employees__extra-button__icon"
            src="/ui-icons/arrow-right.svg"
            alt=""
        />
      </a>
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
.page-block {
  background: var(--color-white);
  border-radius: 6px;
  box-shadow: 0 4px 4px 0 rgba(61, 55, 136, 0.25);
  padding: 12px 19px 17px 17px;
}

.employees {
  &__header {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }

  &__title {
    margin-bottom: 16px;
    font-size: 18px;
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
      font-size: 18px;
      color: #3d3788;
    }
    &__icon {
      width: 28px;
      height: 28px;
    }
  }
}
</style>