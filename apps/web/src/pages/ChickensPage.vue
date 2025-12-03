<template>
  <div class="chickens">
    <div class="chickens__actions">
      <Button
          label="Добавить курицу"
          mode="violet"
          location="page-action"
          @click="showModal = true"
      />
      <Button
          label="Удалить курицу"
          mode="violet"
          location="page-action"
      />
    </div>

    <Modal
      v-if="showModal"
      title="Добавить курицу"
      :form-component="AddChickenForm"
      @close="showModal = false"
      @submit="handleSubmit"
    />

    <ChickensTable
        v-if="loading===false"
        :headers-item="[
            { key: 'name', label: 'Имя' },
            { key: 'breedName', label: 'Порода' },
            { key: 'weight', label: 'Вес' },
            { key: 'eggs', label: 'Яиц в месяц' },
            { key: 'age', label: 'Возраст' },
            { key: 'birthDate', label: 'Дата рождения' },
            { key: 'id', label: 'Ссылка'}
          ]"
        :body-items="chickens"
        :height-size="chickens.length"
    />

    <Loader v-if="loading===true"/>
  </div>
</template>

<script setup>
import Button from "@/components/ui/Button.vue";
import ChickensTable from "@/components/tables/ChickensTable.vue";
import Loader from "@/components/ui/Loader.vue";
import {useChickens} from "@/composables/useChickens.js";
import Modal from "@/components/ui/Modal.vue";
import AddChickenForm from "@/components/forms/AddChickenForm.vue";
import {ref} from "vue";

const {chickens, loading, fetchChickens} = useChickens();
const showModal = ref(false);

const handleSubmit = async () => {
  await fetchChickens();
  showModal.value = false;
};
</script>

<style lang="scss" scoped>
.chickens {
  &__header {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }

  &__title {
    margin-bottom: 16px;
  }

  &__actions {
    margin-bottom: 16px;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    gap: 32px;
  }
}
</style>