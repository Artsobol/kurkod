<script setup>

import Input from "@/components/ui/Input.vue";
import Button from "@/components/ui/Button.vue";
import ChickensTable from "@/components/tables/ChickensTable.vue";
import Loader from "@/components/ui/Loader.vue";
import {useChickens} from "@/composables/useChickens.js";

const {chickens, loading} = useChickens();

</script>

<template>
  <div class="chickens">
    <div class="chickens__actions">
      <Input
          class="chickens__input"
          labelInput="Найти курицу"
      />
      <Button
          label="Добавить курицу"
          mode="violet"
          location="page-action"
      />
      <Button
          label="Удалить курицу"
          mode="violet"
          location="page-action"
      />
    </div>
    <ChickensTable
        v-if="loading===false"
        :headers-item="[
            { key: 'photo', label: 'Фото' },
            { key: 'name', label: 'Имя' },
            { key: 'breedId', label: 'Порода' },
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