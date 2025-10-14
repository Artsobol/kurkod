<template>
  <div class="cells">
    <div class="cells__actions">
      <Input
          class="cells__input"
          labelInput="Найти ячейку"
      />
      <Button
          label="Добавить ячейку"
          mode="violet"
          location="page-action"
      />
      <Button
          label="Удалить ячейку"
          mode="violet"
          location="page-action"
      />
    </div>
  </div>
</template>
<script setup>
import Input from "@/components/ui/Input.vue";
import Button from "@/components/ui/Button.vue";
</script>
<style lang="scss" scoped>
.cells {
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