<template>
  <div class="panel-item">
    <a href="#" class="panel-item__link">
      <img
          class="panel-item__image"
          :src="`/${image}.png`"
          :alt="title"
          alt=""
          width="48"
          height="48"
      >
      <span class="panel-item__title">
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
  image: {
    type: String,
    required: false
  },
  href: {
    type: String,
    required: true
  }
})
</script>

<style lang="scss" scoped>
.panel-item__link {
  display: flex;
  gap: 20px;
  align-items: center;

  font-weight: 500;
  font-size: 22px;
  color: var(--color-brown-light);

  @include laptop {
    font-size: 18px;
    gap: 16px;
  }
}
</style>