<template>
  <a
      class="info-block"
      :href="href"
  >
    <span class="info-block__title">
      {{ title }}
    </span>
    <div class="info-block__container">
       <span v-if="subTitle" class="info-block__subtitle">
      {{ subTitle }}
    </span>
      <span v-if="count" class="info-block__count">
      {{ count }}
    </span>
    </div>
  </a>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    required: true
  },
  href: {
    type: String,
    required: true
  },
  subTitle: {
    type: String,
    required: false
  },
  count: {
    type: String,
    required: false
  }
})
</script>

<style lang="scss" scoped>
.info-block {
  width: 461px;
  height: 121px;
  padding: 17px 15px 6px 26px;
  color: var(--color-white);
  background-color: var(--color-purple-contrast);
  box-shadow: 0 4px 4px 0 rgba(0, 0, 0, 0.25);
  border-radius: 8px;

  &__container {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }

  &__title {
    font-weight: 600;
    font-size: 28px;
  }

  &__subtitle {
    font-weight: 500;
    font-size: 20px;
    opacity: 0.8;
  }

  &__count {
    font-weight: 600;
    font-size: 48px;
  }
}
</style>