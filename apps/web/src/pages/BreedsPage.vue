<template>
  <div class="breeds">
    <div class="breeds__actions">
      <Input
          class="breeds__input"
          labelInput="Найти породу"
      />
      <Button
          label="Добавить породу"
          mode="violet"
          location="page-action"
      />
      <Button
          label="Удалить породу"
          mode="violet"
          location="page-action"
      />
    </div>
    <BreedsTable
        v-if="loading===false"
        :headers-item="[
            { key: 'name', label: 'Название' },
            { key: 'eggsNumber', label: 'Яиц в месяц' },
            { key: 'weight', label: 'Вес' },
            { key: 'id', label: 'Ссылка'}
          ]"
        :body-items="breeds"
        :height-size="breeds.length"
    />

    <Loader v-if="loading===true"/>
  </div>
</template>
<script setup>
import Input from "@/components/ui/Input.vue";
import Button from "@/components/ui/Button.vue";
import Loader from "@/components/ui/Loader.vue";
import BreedsTable from "@/components/tables/BreedsTable.vue";
import useBreeds from "@/composables/useBreeds.js";

const {breeds, loading} = useBreeds();
</script>
<style lang="scss" scoped>
.breeds {
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