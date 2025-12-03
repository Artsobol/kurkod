<template>
  <form @submit.prevent="addCage" class="add-form">
    <label class="add-form__label">
      <span class="add-form__label-title">Номер клетки:</span>
      <input
          class="add-form__input"
          type="number"
          min="0"
          v-model="newCage.cageNumber"
          placeholder="Введите номер клетки"
          required
      />
    </label>

    <Button
        label="Добавить"
        mode="violet"
        location="page-action"
        type="submit"
        style="align-self: center; margin-top: 24px;"
    />
  </form>
</template>

<script setup>
import { ref } from "vue";
import { createCage } from "@/api/cages.js";
import Button from "@/components/ui/Button.vue";

const emit = defineEmits(["close", "submit"]);
const props = defineProps({
  rowId: {
    type: Number,
    required: true,
  },
});

const newCage = ref({
  cageNumber: "",
});

const addCage = async () => {
  try {
    const cageToSend = {
      cageNumber: Number(newCage.value.cageNumber),
    };

    await createCage(props.rowId, cageToSend);
    emit("submit", cageToSend);
    emit("close");
  } catch (error) {
    console.error("Ошибка при добавлении клетки:", error);
  }
};
</script>