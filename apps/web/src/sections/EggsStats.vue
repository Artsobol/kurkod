<template>
  <section class="eggs-stats">
    <div class="eggs-stats__container">
      <h2>
        Eggs Stats
      </h2>
      <Button
          mode="transparent"
          label="See all →"
          location="home"
          href="#"
      />
    </div>

    <div class="eggs-stats__content">
      lalaalal
    </div>
  </section>
</template>

<script setup>
import Button from "@/components/ui/Button.vue";
</script>

<style lang="scss" scoped>
.eggs-stats {
  width: 635px;

  &__container {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 14px;
  }

  &__content {
    background: var(--color-white);
    border-radius: 25px;
    height: 276px;
    box-shadow: var(--shadow);
  }
}
</style>