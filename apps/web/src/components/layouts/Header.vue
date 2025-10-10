<template>
  <header class="header">
    <div class="header__left-container">
      <Logo/>
      <Button
          class="header__burger"
          @click="$emit('toggle')"
          style="display: flex; justify-content: center; flex-direction: row; align-items: center;"
      >
        <Icon name="burger-button"
              width="28"
              height="21"
        />
      </Button>
    </div>

    <div class="header__nav">
      <Button
          class="header__nav-link"
          @click="ui.toggleTheme"
      >
        <Icon :name="ui.theme === 'light' ? 'light' : 'dark'" class="header__nav-link-icon"/>
      </Button>
      <Button
          class="header__nav-link"
      >
        <Icon name="switch-language"/>
      </Button>
      <Button
          class="header__nav-link"
      >
        <Icon name="notifications"/>
      </Button>
      <Button
          class="header__nav-link"
      >
        <Icon name="settings"/>
      </Button>
      <a class="header__nav-link" href="#">
        <img
            class="header__nav-link-icon"
            src="/ui-icons/settings.svg"
            alt=""
        />
      </a>

    </div>
  </header>
</template>

<script setup>

import Logo from "@/components/ui/Logo.vue";
import Button from "@/components/ui/Button.vue";
import {uiStore} from "@/stores/ui.js";
import SunIcon from '@/assets/icons/light.svg'
import MoonIcon from '@/assets/icons/dark.svg'
import Icon from "@/components/ui/Icon.vue";

const ui = uiStore()
</script>

<style lang="scss" scoped>
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 79px;
  background-color: var(--section-bg);
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  gap: 53px;
  align-items: center;
  padding: 15px 29px 16px 31px;
  box-shadow: var(--shadow);
  z-index: 11;

  &__nav {
    display: flex;
    align-items: center;
    gap: 35px;
  }

  &__nav-link {
    background-color: var(--header-icons-bg);
    border-radius: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 40px;
    height: 40px;
  }

  &__nav-link-icon {
    width: 24px;
    height: 24px;
  }

  &__left-container {
    display: flex;
    gap: 52px;
  }
}

</style>
