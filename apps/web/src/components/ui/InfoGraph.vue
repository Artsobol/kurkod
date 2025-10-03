<template>
  <div class="info-graph">
    <div class="info-graph__header">
      <h2 class="info-graph__title">
        {{title}}
      </h2>
      <a class="info-graph__button" :href="href">
        <img
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
  width: 709px;
  height: 569px;
  background-color: var(--color-white);
  padding: 26px 52px 42px 45px;
  box-shadow: 0 4px 4px 0 rgba(0, 0, 0, 0.25);
  border-radius: 16px;

  @include desktop-m {
    width: 585px;
    height: 492px;
    padding: 29px 20px 41px 31px;
  }

  @include desktop {
    width: 461px;
    height: 410px;
    padding: 29px 15px 33px 22px;
  }

  &__header {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    padding-bottom: 31px;
  }

  &__image-container {
    width: 612px;
    height: 431px;
    overflow: hidden;
    display: flex;
    justify-content: center;
    align-items: center;

    @include desktop-m {
      width: 508px;
      height: 358px;
    }

    @include desktop {
      width: 424px;
      height: 298px;
    }
  }

  &__image-container img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  &__title {
    font-weight: 700;
    font-size: 32px;

    @include desktop-m {
      font-size: 28px;
    }

    @include desktop {
      font-size: 22px;
    }
  }

  &__button {
    width: 40px;
    height: 40px;
  }
}
</style>