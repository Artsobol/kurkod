<template>
  <a
      v-if="href"
      :href="href"
      :class="[mode, location]"
  >
    <slot>{{ label }}</slot>
  </a>

  <button
      v-else
      type="button"
      :class="[mode, location]"
      @click="handleClick"
  >
    <slot>{{ label }}</slot>
  </button>
</template>

<script setup>
const emit = defineEmits(['click'])
const handleClick = (event) => {
  emit('click', event)
}
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

//Mode

.transparent {
}

.violet {
  background-color: #726DAF;
  color: var(--color-white);
}

//Location

.page-action {
  font-weight: 700;
  font-size: 16px;
  width: 248px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding-block: 16px;
  border-radius: 8px;
  box-shadow: var(--shadow);

}

</style>