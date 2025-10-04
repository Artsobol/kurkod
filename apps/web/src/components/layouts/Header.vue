<template>
  <header class="header">
    <div class="header__logo">
      <Logo/>
    </div>
    <button class="toggle-btn" @click="$emit('toggle')">☰</button>

    <div class="header__nav">
      <a class="header__nav-link" href="#">
        <img
            src="/ui-icons/light.svg"
            alt=""
        />
      </a>
      <a class="header__nav-link" href="#">
        <img
            src="/ui-icons/switch-language.svg"
            alt=""
        />
      </a>
      <a class="header__nav-link" href="#">
        <img
            src="/ui-icons/notifications.svg"
            alt=""
        />
      </a>
      <a class="header__nav-link" href="#">
        <img
            src="/ui-icons/settings.svg"
            alt=""
        />
      </a>
      <a class="header__nav-link" href="#">
        <img
            src="/ui-icons/settings.svg"
            alt=""
        />
      </a>

    </div>
  </header>
</template>

<script setup>

import Logo from "@/components/ui/Logo.vue";

</script>

<style lang="scss" scoped>
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 79px;
  background-color: var(--color-white);
  font-family: var(--font-family-poppins);
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
    background-color: var(--color-bg);
    border-radius: 100%;
    padding: 8px;
  }
}

</style>
