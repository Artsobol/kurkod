<template>
  <section class="employees">
    <div class="employees__container">
      <h2>
        Employees
      </h2>
      <Button
          mode="transparent"
          label="See all →"
          location="home"
          href="#"
      />
    </div>

    <div class="employees__content">
      lalla
    </div>
  </section>
</template>

<script setup>
import Button from "@/components/ui/Button.vue";
</script>

<style lang="scss" scoped>
.employees {
  min-width: 445px;
  width: 100%;

  @include desktop {
  }

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
    height: 576px;
    box-shadow: var(--shadow);
  }
}
</style>