<template>
  <div class="sign-in">
    <div class="sign-in__window">
      <div class="sign-in__window__first-column">
        <Logo
            :custom-style="true"
        />
        <p class="sign-in__description-first">
          Контролируйте производство,
          персонал и отчётность
          в одном месте.
        </p>
        <Button
            label="Получить доступ"
            mode="white-no-switch"
            location="sign-in-button"
            style="width: 262px;"
        />
        <p class="sign-in__copyright">
          © 2025 КурКод
        </p>
      </div>
      <div class="sign-in__window__second-column">
        <div class="sign-in__window__buttons">
          <Button
              class="header__nav-link"
              @click="ui.toggleTheme"
              :icon-name="ui.theme === 'light' ? 'light' : 'dark'"
          />
          <Button
              class="header__nav-link"
              icon-name="switch-language"
          />
        </div>
        <h2 class="sign-in__title">
          Авторизация
        </h2>
        <p class="sign-in__text">
          Для входа в систему укажите корпоративную почту и пароль.
        </p>
        <div>
          <span style="display: flex; align-items: center; justify-content: center; padding-top: 24px; gap: 12px;">
            <span class="sign-in__text">
              Войти как:
            </span>
            <select class="sign-in__select">
              <option class="sign-in__select__option">Директор</option>
              <option class="sign-in__select__option">Сотрудник</option>
            </select>
          </span>
          <form action="#" method="post" class="sign-in__form">
            <input class="sign-in__input" type="email" name="auth_email" placeholder="Почта" required>
            <input class="sign-in__input" type="password" name="auth_pass" placeholder="Пароль" required>
            <a href="#" class="sign-in__text">Забыли пароль?</a>
            <Button
                type="submit"
                name=""
                label="Войти"
                mode="violet-no-switch"
                location="sign-in-button"
                style="width: 339px;"
            />
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>

import Logo from "@/components/ui/Logo.vue";
import Button from "@/components/ui/Button.vue";

import {uiStore} from "@/stores/ui.js";

const ui = uiStore()
</script>

<style lang="scss" scoped>
.sign-in {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 85vh;

  &__window {
    width: 1100px;
    height: 514px;
    border-radius: 16px;
    overflow: hidden;
    background: var(--section-bg);
    display: grid;
    grid-template-columns: 332px 768px;

    &__first-column {
      background-color: var(--color-section-contrast-light);
      color: var(--color-white);
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      padding-top: 56px;
      padding-bottom: 24px;
      padding-inline: 35px;
    }

    &__second-column {
      display: flex;
      flex-direction: column;
      justify-content: start;
      align-items: center;
      padding-top: 18px;
      padding-right: 24px;
    }

    &__buttons {
      display: flex;
      flex-direction: row;
      justify-content: flex-end;
      align-self: flex-end;
      gap: 24px;
    }
  }

  &__description-first {
    font-weight: 600;
    font-size: 20px;
    line-height: 200%;
    text-align: center;
    max-width: 262px;
    padding-top: 49px;
    padding-bottom: 60px;
  }

  &__copyright {
    font-weight: 400;
    font-size: 14px;
    color: #fff;
    text-align: left;
    align-self: flex-start;
    padding-top: 51px;
  }

  &__title {
    font-weight: 700;
    font-size: 24px;
    padding-bottom: 30px;
  }

  &__text {
    font-weight: 500;
    font-size: 14px;
    color: var(--color-text-light);
  }

  &__form {
    display: flex;
    flex-direction: column;
    justify-content: center;
    gap: 16px;
    padding-block: 16px;
  }

  &__input {
    border-radius: 8px;
    width: 339px;
    height: 48px;
    background: var(--color-bg-light);
    display: flex;
    flex-direction: row;
    align-items: center;
    padding-inline: 24px;
  }

  &__select {
  }

}

</style>