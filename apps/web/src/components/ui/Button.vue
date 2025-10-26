<template>
  <a
      v-if="href"
      :href="href"
      :class="[mode, location, 'button']"
  >
<!--    <template v-if="specificButton === 'arrow-right'">-->
<!--      <span v-if="label" class="arrow-right__title">{{ label }}</span>-->
<!--      <Icon name="arrow-right" width="28" height="28"/>-->
<!--    </template>-->
    <slot>{{ label }}</slot>
    <template v-if="iconName">
      <Icon :name="iconName" :width="iconWidth" :height="iconHeight"/>
    </template>
  </a>

  <button
      v-else
      type="button"
      :class="[mode, location, 'button']"
      @click="handleClick"
  >
    <slot>{{ label }}</slot>
    <template v-if="iconName">
      <Icon :name="iconName" :width="iconWidth" :height="iconHeight"/>
    </template>
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
  },
  iconName: {
    type: String,
    required: false
  },
  iconWidth: {
    type: Number,
    default: 28
  },
  iconHeight: {
    type: Number,
    default: 28
  }
})
</script>

<style lang="scss" scoped>
.button {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  gap: 14px;
  color: var(--color);
}

//Mods

.violet {
  background-color: var(--contrast);
  color: var(--color-white);
}

.white-no-switch {
  background-color: var(--color-white);
  color: var(--color-section-contrast-light)
}

.violet-no-switch {
  background-color: var(--color-section-contrast-light);
  color: var(--color-white);
}

//Locations

.page-action {
  font-weight: 700;
  font-size: 16px;
  width: 248px;
  padding-block: 16px;
  border-radius: 8px;
}

.block-action {
  font-weight: 600;
  font-size: 14px;
  width: 194px;
  border-radius: 8px;
  padding-block: 8px;
}

.sign-in-button {
  border-radius: 8px;
  // width: 262px;
  height: 48px;
  font-weight: 700;
  font-size: 14px
}

.close {
  width: 24px;
  height: 24px;
}

</style>