<template>
  <div class="table-template">
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
        <EmployeeItem
            photo="/images/user-photo.jpg"
            name="Выговорите Макан Яговорю"
            position="Работник птицефермы"
            status="На работе"
        />
        <EmployeeItem
            photo="/images/user-photo.jpg"
            name="Выговорите Макан Яговорю"
            position="Работник птицефермы"
            status="На работе"
        />
        <EmployeeItem
            photo="/images/user-photo.jpg"
            name="Выговорите Макан Яговорю"
            position="Работник птицефермы"
            status="На работе"
        />
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
.table-template th,
.table-template td {
  padding-block: 10px;
  text-align: center;
  border-radius: 4px;
  vertical-align: middle;
}

.table-template__body {
  font-size: 14px;
}

.table-template {
  width: 100%;

  &__body table {
    width: 100%;
    border-collapse: collapse;
    table-layout: fixed;
  }
}

</style>