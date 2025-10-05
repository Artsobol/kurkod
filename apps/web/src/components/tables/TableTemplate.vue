<template>
  <div class="table-template">
    <div class="table-template__header">
      <h2 class="table-template__title">
        {{ title }}
      </h2>
      <a class="table-template__button" href="#">
        <img
            class="table-template__button__img"
            src="/ui-icons/arrow-right.svg"
            alt=""
        />
      </a>
    </div>
    <div class="table-template__body">
      <table>
        <thead>
        <tr>
          <th v-for="(headerItem, index) in headersItem" :key="index">
            {{ headerItem }}
          </th>
        </tr>
        </thead>
        <tbody>
        <EmployeeItem
            photo="/images/user-photo.jpg"
            name="Выговорите Макан Яговорю"
            position="Работник птицефермы"
            status="На работе"
        />
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import EmployeeItem from "@/components/tables/EmployeeItem.vue";

defineProps({
  title: {
    type: String,
    required: true,
  },
  heightSize: {
    type: Number,
    default: 5,
  },
  stylesPack: {
    type: String,
    required: true,
  },
  headersItem: {
    type: Array,
    required: true,
  },
  bodyItems: {
    type: Array,
    required: true,
  }
})
</script>

<style scoped lang="scss">
.table-template {
  width: 100%;
  &__header {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }
  &__body table {
    width: 100%;
    border-collapse: collapse;
    table-layout: fixed;
  }
}

.table-template th,
.table-template td {
  padding: 10px;
  text-align: center;
  border-radius: 4px;
}

.table-template td {
}
.table-template tbody tr {
  position: relative;
  left: 30px;
}
</style>