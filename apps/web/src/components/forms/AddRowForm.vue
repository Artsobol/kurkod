<template>
  <form @submit.prevent="addRow" class="add-form">
    <label class="add-form__label">
      <span class="add-form__label-title">Номер ряда:</span>
      <input
          class="add-form__input"
          type="number"
          min="0"
          v-model="newRow.rowNumber"
          placeholder="Введите номер ряда"
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
import { createRow } from "@/api/rows.js";
import Button from "@/components/ui/Button.vue";

const emit = defineEmits(["close", "submit"]);
const props = defineProps({
  workshopId: {
    type: Number,
    required: true,
  },
});

const newRow = ref({
  rowNumber: "",
});

const addRow = async () => {
  try {
    const rowToSend = {
      rowNumber: Number(newRow.value.rowNumber),
    };

    await createRow(props.workshopId, rowToSend);
    emit("submit", rowToSend);
    emit("close");
  } catch (error) {
    console.error("Ошибка при добавлении ряда:", error);
  }
};
</script>
