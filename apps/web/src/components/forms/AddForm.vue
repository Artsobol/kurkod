<template>
  <div class="modal-overlay" @click.self="close">
    <div class="modal">
      <h2 style="font-size: 24px; padding-bottom: 24px;">
        {{ title }}
      </h2>
      <h2 class="modal-title">{{ title }}</h2>
      <Button
          class="modal-close"
          @click="close"
          icon-name="close"
          icon-width="32"
          icon-height="32"
      />
      <component :is="formComponent" @submit="onFormSubmit"/>
    </div>
  </div>
</template>
<script setup>
import Button from "@/components/ui/Button.vue";

defineProps({
  title: {
    type: String,
    required: true
  },
  formComponent: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(["close", "submit"]);
const close = () => emit("close");
const onFormSubmit = (payload) => emit("submit", payload);

</script>