<template>
  <div class="chicken-table-item">
    <div v-if="typeOfChicken === 'chicken'" class="chicken-table-item__content">
      <div class="chicken-table-item__image-container">
        <img
            :src="photo"
            :alt="name"
        />
      </div>
      <span>
        {{name}}
      </span>
      <span>
        {{breed}}
      </span>
      <span>
        {{weight}} kg
      </span>
      <span>
        {{age}}
      </span>
      <span>
        {{gender}}
      </span>
      <span v-if="eggsPerMonth">
        {{eggsPerMonth}}
      </span>
      <span v-else>
        -
      </span>
    </div>

    <div v-else-if="typeOfChicken === 'chick'" class="chicken-table-item__content">
      🐥 Это цыплёнок
    </div>

    <div v-else-if="typeOfChicken === 'rooster'" class="chicken-table-item__content">
      🐓 Это петух
    </div>

    <div v-else>
      ❓ Неизвестный тип птицы
    </div>
  </div>
</template>

<script setup lang="ts">
interface ChickenProps {
  typeOfChicken: string
  photo?: string
  name?: string
  breed?: string
  weight?: number
  age?: number
  gender?: string
  eggsPerMonth?: number
  cage?: string
}

defineProps<ChickenProps>()
</script>

<style lang="scss" scoped>
.chicken-table-item {
  &__image-container {
    width: 64px;
    height: 64px;
    object-fit: contain;
    background: red;
    border-radius: 100%;
  }

  &__content {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    gap: 45px;
  }

}


</style>