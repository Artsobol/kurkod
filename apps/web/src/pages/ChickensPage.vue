<template>
  <div class="chickens">
    <div class="chickens__actions">
      <Input
          class="chickens__input"
          labelInput="Найти курицу"
      />
      <Button
          label="Добавить курицу"
          mode="violet"
          location="page-action"
      />
      <Button
          label="Удалить курицу"
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
.chickens {
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