<template>
  <header class="header">
    <div class="header__left-container">
      <Logo/>
      <Button
          class="header__burger"
          @click="$emit('toggle')"
          iconName="burger-button"
          :icon-width="28"
          :icon-height="21"
      />
      <h1>
        {{ pageTitle }}
      </h1>
    </div>

    <div class="header__nav">
      <Button
          class="header__nav-link"
          @click="ui.toggleTheme"
          :icon-name="ui.theme === 'light' ? 'light' : 'dark'"
      />
<!--      <Button-->
<!--          class="header__nav-link"-->
<!--          icon-name="switch-language"-->
<!--      />-->
<!--      <Button-->
<!--          class="header__nav-link"-->
<!--          icon-name="notifications"-->
<!--      />-->
<!--      <Button-->
<!--          class="header__nav-link"-->
<!--          icon-name="settings"-->
<!--      />-->
      <Button
          class="header__nav-link"
          icon-name="user"
          href="/account"
      />

    </div>
  </header>
</template>

<script setup>
import Logo from "@/components/ui/Logo.vue";
import Button from "@/components/ui/Button.vue";
import {uiStore} from "@/stores/ui.js";
import {computed} from "vue";
import {useRoute} from "vue-router";

const ui = uiStore()
const route = useRoute();

const pageTitle = computed(() => route.name || "");
</script>

<style lang="scss">
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
  z-index: 11;
  border-bottom: var(--border);

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

  &__left-container {
    display: flex;
    align-items: center;
    gap: 52px;
  }
}

</style>
