<template>
  <div class="sign-in">
    <div class="sign-in__window">
      <div class="sign-in__window__first-column">
        <Logo />
        <p>
          Контролируйте производство,
          персонал и отчётность
          в одном месте.
        </p>
        <Button
          label="Получить доступ"
        />
        <p>
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
        <h2>
          Авторизация
        </h2>
        <p>
          Для входа в систему укажите корпоративную почту и пароль.
        </p>
        <div>
          <span>
            Войти как:
          </span>
          <select>
            <option>Директор</option>
            <option>Сотрудник</option>
          </select>
          <form action="#" method="post">
            <input type="email" name="auth_email" placeholder="Почта" required >
            <input type="password" name="auth_pass" placeholder="Пароль" required >
            <a href="#">Забыли пароль?</a>
            <Button
              type="submit"
              name=""
              label="Войти"
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
  &__window {
    border-radius: 16px;
    background: var(--section-bg);
    display: grid;
    grid-template-columns: 331px 769px;
  }
}

</style>