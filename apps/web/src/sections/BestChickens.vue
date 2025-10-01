<template>
  <section class="best-chickens">
    <div class="best-chickens__container">
      <h2>
        Best Chickens
      </h2>
      <Button
        mode="transparent"
        label="See all →"
        location="home"
        href="#"
      />
    </div>

    <div class="best-chickens__content">
      <ChickenTable size="small" />
    </div>
  </section>
</template>

<script setup>

import Button from "@/components/ui/Button.vue";
import ChickenTable from "@/components/tables/ChickenTable.vue";
</script>

<style lang="scss" scoped>
.best-chickens {
  min-width: 1367px;
  width: 100%;
  @include desktop {
    min-width: 1117px;
  }
  &__container {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 18px;
  }
  &__content {
    display: flex;
    justify-content: center;
    align-items: center;
    background: var(--color-white);
    border-radius: 25px;
    height: 322px;
    box-shadow: var(--shadow);
  }
}
</style>