<template>
  <div class="info-graph">
    <div class="info-graph__header">
      <h2 class="info-graph__title">
        {{title}}
      </h2>
      <a class="info-graph__button" :href="href">
        <img
            class="info-graph__button__img"
            src="/ui-icons/arrow-right.svg"
            alt=""
        />
      </a>
    </div>

    <div class="info-graph__image-container">
      <img
        :src="image"
        alt=""
      />
    </div>
  </div>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    required: true,
  },
  href: {
    type: String,
    required: true,
  },
  image: {
    type: String,
    required: true,
  }
})
</script>

<style lang="scss" scoped>
.info-graph {
  &__header {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    padding-bottom: 20px;
  }

  &__title {
    font-size: 16px;
  }

  &__button__img {
    width: 28px;
    height: auto;
  }
}
</style>