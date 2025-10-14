<template>
  <div class="diets">
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
  </div>
</template>
<script setup>
import Input from "@/components/ui/Input.vue";
import Button from "@/components/ui/Button.vue";

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