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
        <div class="account__content page-block">
          <div class="account__img-container">
            <img
                src="/images/user-photo.jpg"
                alt="user-photo"
                class="account__img"
            />
          </div>

          <div class="account__info">
            <span class="h2 text-ellipsis">Фамилия</span>
            <span class="h2 text-ellipsis">Имя</span>
            <span class="h2 text-ellipsis">Отчество</span>
          </div>


          <div class="account__info">
            <span class="h2">Директор предприятия</span>
            <span style="display: flex; flex-direction: row; gap: 16px; align-items: center;">
              <span class="account__info-text text-ellipsis" style="max-width: 27.8vw">emailemailemail@gmail.com</span>
              <Button
                  icon-name="hide"
                  :icon-width="20"
                  :icon-height="20"
              />
            </span>
            <span style="display: flex; flex-direction: row; gap: 16px; align-items: center;">
              <span class="account__info-text">+* (***) *** - ** - **</span>
              <Button
                  icon-name="show"
                  :icon-width="20"
                  :icon-height="20"
              />
            </span>

          </div>

        </div>
      </div>

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
    align-items: center;

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
</style>