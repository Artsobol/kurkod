<template>
  <div class="account">
    <div class="account__first-row">
      <div class="account__block">
        <div class="account__header">
          <h2 class="account__title">Основная информация</h2>
          <Button
              label="Редактировать"
              mode="violet"
              location="block-action"
          />
        </div>
        <div class="account__content">
          lalala
        </div>
      </div>
      <div class="account__block">
        <div class="account__header">
          <h2 class="account__title">Основная информация</h2>
          <Button
              label="Редактировать"
              mode="violet"
              location="block-action"
          />
        </div>
        <div class="account__content">
          lalala
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>

import Button from "@/components/ui/Button.vue";
</script>
<style lang="scss" scoped>
.account {
  &__header {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
  }
  &__first-row {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 32px;
  }
  &__content {
    background: var(--section-bg);
  }
}
</style>