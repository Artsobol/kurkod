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
      <span v-if="count && !loading" class="info-block__count">
      {{ count }}
    </span>
      <div v-if="loading" class="info-block__loader">
        <div class="loader"></div>
      </div>
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
  },
  loading: {
    type: Boolean,
    default: false
  }
})
</script>

<style lang="scss" scoped>
.info-block {
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  width: 100%;
  box-sizing: border-box;
  gap: 18px;
  color: var(--color-white);

  &__container {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }

  &__title {
    font-weight: 600;
    font-size: 14px;
  }

  &__subtitle {
    font-weight: 500;
    opacity: 0.8;
    font-size: 14px;
  }

  &__count {
    font-weight: 600;
    font-size: 23px;
  }

  &__loader {
    display: flex;
    align-items: center;
    justify-content: center;
  }

  &__loader .loader {
    width: 24px;
    height: 24px;
    border: 3px solid rgba(255, 255, 255, 0.2);
    border-top-color: var(--color-white);
    border-radius: 50%;
    animation: spin 1s linear infinite;
  }
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}
</style>