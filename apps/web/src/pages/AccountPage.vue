<template>
  <div class="account">
    <div class="account__first-row">
      <UserInfo/>

      <div class="account__block">
        <div class="account__header">
          <h2 class="account__title">Безопасность</h2>
          <Button
              label="Настроить"
              mode="violet"
              location="block-action"
          />
        </div>
        <div class="account__content page-block">

        </div>
      </div>
    </div>
    <div class="account__first-row">

      <div class="account__block">
        <div class="account__header">
          <h2 class="account__title">Уведомления</h2>
        </div>
        <div class="account__content page-block">

        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import Button from "@/components/ui/Button.vue";
import UserInfo from "@/sections/UserInfo.vue";
</script>

<style lang="scss" scoped>
.account {
  &__first-row {
    display: grid;
    grid-template-columns: 1.8fr 1fr;
    gap: 32px;
    margin-bottom: 32px;
  }

  &__header {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
  }

  &__content {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-evenly;
    gap: 16px;
  }

  &__info {
    display: flex;
    flex-direction: column;
    gap: 24px;

    &-text {
      text-wrap: wrap;
    }
  }

  &__img-container {
    width: 10vw;
    height: 10vw;
    overflow: hidden;
    border-radius: 8px;
  }

  &__img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    display: block;
  }
}

.letter-spacing-email {
  letter-spacing: 0.155em;
}

.letter-spacing-phone {
  letter-spacing: 0.1em;
}
</style>