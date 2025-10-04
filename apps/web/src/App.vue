<script setup>
import { useRoute } from 'vue-router'
import Header from './components/layouts/Header.vue'
import Panel from "@/components/layouts/Panel.vue";
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
      <div class="page-block">
        <h3>Заголовок блока</h3>
        <p>Здесь текст или контент страницы.</p>
      </div>

      <div class="page-block">
        <h3>Ещё блок</h3>
        <p>При сворачивании меню ширина контента растянется.</p>
      </div>
    </Content>

  </div>
<!--  <div class="layouts">-->
<!--    <Header />-->
<!--    <Panel></Panel>-->
<!--  </div>-->
<!--  <div class="content">-->
<!--    <HomePage />-->
<!--  </div>-->
</template>

<style lang="scss" scoped>
.wrapper {
  display: flex;
  height: 100vh;
  width: 100%;
  overflow: hidden;
  margin-top: 79px;
}

.page-block {
  margin: 20px 0;
  padding: 20px;
  background: white;
  border-radius: 6px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}
</style>
