<template>
  <a
      v-if="href"
      :href="href"
      :class="[mode, location]"
  >
    {{ label }}
  </a>

  <button
      v-else
      type="button"
      :class="[mode, location]"
  >
    {{ label }}
  </button>
</template>

<script setup>
defineProps({
  label: {
    type: String,
    required: false
  },
  mode: {
    type: String,
    required: false
  },
  location: {
    type: String,
    required: false
  },
  href: {
    type: String,
    required: false
  }
})
</script>

<style lang="scss" scoped>
.transparent {
  background-color: transparent;
  color: var(--color-sand-dark)
}
.home {
  font-weight: 500;
  font-size: 18px;
}
</style>