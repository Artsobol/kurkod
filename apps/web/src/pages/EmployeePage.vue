<template>
  <UserInfo
      v-if="user"
      :photo="user.photo"
      :surname="user.surname"
      :name="user.name"
      :patronymic="user.patronymic"
      :position="user.position"
      :salary="user.salary"
      :phone="user.phone"
      :email="user.email"
      :status="user.status"
      />
</template>

<script setup>
import UserInfo from "@/sections/UserInfo.vue";
import employees_data from "@/constants/EMPLOYEES_DATA.json";
import { useRoute } from 'vue-router';

const users = employees_data;
const route = useRoute();
const userId = Number(route.params.id);
const user = users.find(u => u.id === userId);
</script>

<style lang="scss" scoped>

</style>