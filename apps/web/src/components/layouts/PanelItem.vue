<template>
  <div class="panel-item">
    <a href="#" class="panel-item__link">
      <span
          class="panel-item__title"
          :class="{ 'panel-item__title--main': isMainTitle }"
      >
        {{ title }}
      </span>
    </a>


  </div>
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
  isMainTitle: {
    type: Boolean,
    default: false,
  }
})
</script>

<style lang="scss" scoped>
.panel-item__link {
  display: flex;
  gap: 20px;
  align-items: center;

  .panel-item__title {
    font-weight: 500;
    font-size: 24px;
    color: var(--color-text-light);
    padding-left: 44px;
  }

  .panel-item__title--main {
    color: var(--color-text);
    font-weight: 600;
    padding-left: 0;
    padding-top: 12px;
  }
}
</style>