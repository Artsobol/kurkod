<script setup>
import { useRoute } from 'vue-router'
import Header from './components/layouts/Header.vue'
import HomePage from "@/pages/HomePage.vue";
import Sidebar from "@/components/layouts/Sidebar.vue";
import Content from "@/components/layouts/Content.vue";
const route = useRoute()

import {ref} from "vue";
const isClosed = ref(false);

function toggleSidebar() {
  isClosed.value = !isClosed.value;
}
</script>

<template>
  <Header @toggle="toggleSidebar"/>
  <div class="wrapper">
    <Sidebar
      :isClosed="isClosed"
    />
    <Content>
      <HomePage />
    </Content>
  </div>
</template>

<style lang="scss" scoped>
.wrapper {
  display: flex;
  width: 100%;
  overflow-x: hidden;
  margin-top: 79px;
  height: calc(100vh - 79px);
}


</style>
