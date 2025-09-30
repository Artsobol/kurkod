<template>
  <header class="header" title="">
    <div class="header__logo">
      <img
          src="/logo.png"
          width="64"
          height="64"
          alt="logo"
      />
      <span class="header__logo-text">
        ChickCode
      </span>
    </div>

    <h1 class="header__title">
      {{ title }}
    </h1>

    <div class="header__nav">
      <div class="header__nav-actions">
        <form
            class="header__form"
            action="/search" method="get">
          <img
              class="header__form-icon"
              src="/ui-icons/search.svg"
              alt=""
          />
          <input
              class="header__form-input"
              type="text"
              name="query"
              placeholder="Search for something"/>
        </form>

        <a class="header__action-link" href="#">
          <img
              src="/ui-icons/setting.svg"
              alt=""
          />
        </a>

        <a class="header__action-link" href="#">
          <img
              src="/ui-icons/user.svg"
              alt=""
          />
        </a>
      </div>
    </div>
  </header>
</template>

<script setup>

defineProps({
  title: {
    type: String,
    required: true
  }
})
</script>

<style lang="scss" scoped>
.header {
  background-color: var(--color-white);
  font-family: var(--font-family-poppins);
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  gap: 53px;
  align-items: center;
  padding: 16px 40px 20px 20px;
  border-bottom: 1px solid #e6eff5;

  &__title {
    margin-right: auto;
    font-weight: 600;
    font-size: 28px;
    color: var(--color-brown-dark);
  }

  &__logo {
    display: flex;
    align-items: center;
    gap: 11px;
    font-weight: 700;
    font-size: 25px;
    color: var(--color-brown-dark);
  }

  &__nav {
    display: flex;
    align-items: center;
    gap: 40px;
  }

  &__nav-actions {
    display: flex;
    align-items: center;
    margin-left: auto;
    gap: 25px;
  }

  &__form {
    width: 255px;
    height: 50px;

    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: center;
    gap: 15px;

    font-weight: 400;
    font-size: 15px;
    color: var(--color-sand-dark);

    padding-right: 33px;
    padding-left: 25px;
    border-radius: 40px;

    background-color: var(--color-sand-light);
  }

  &__action-link {
    background-color: var(--color-sand-light);
    border-radius: 100%;
    padding: 13px;
  }
}

</style>
