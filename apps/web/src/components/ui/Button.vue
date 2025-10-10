<template>
  <a
      v-if="href"
      :href="href"
      :class="[mode, location, 'button']"
  >
    <template v-if="specificButton === 'arrow-right'">
      <span v-if="label" class="arrow-right__title">{{ label }}</span>
      <Icon name="arrow-right" width="28" height="28"/>
    </template>
    <template v-else>
      {{ label }}
    </template>
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
import Icon from "@/components/ui/Icon.vue";

const emit = defineEmits(['click'])
const handleClick = (event) => {
  emit('click', event)
}
defineProps({
  label: {
    type: String,
    required: false
  },
  specificButton: {
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
.button {
  display: flex;
  justify-content: center;
  align-items: center;
  color: var(--color);
}

//specifics
.button .arrow-right__title {
  font-size: 16px;
  font-weight: 700;
  margin-right: 14px;
}

.arrow-right {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  gap: 14px;

  &__title {
    font-size: 16px;
    font-weight: 700;
  }
}

//Mods

.transparent {
}

.violet {
  background-color: var(--contrast);
  color: var(--color-white);
}

//Locations

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