<script setup>
import { useRoute } from 'vue-router'
import Header from './components/layouts/Header.vue'
import HomePage from "@/pages/HomePage.vue";
import Sidebar from "@/components/layouts/Sidebar.vue";
import Content from "@/components/layouts/Content.vue";
import {uiStore} from "@/stores/ui.js";

const route = useRoute()
const ui = uiStore()

</script>

<template>
  <Header @toggle="ui.toggleSidebar()"/>
  <div class="wrapper">
    <Sidebar
      :isClosed="ui.isSidebarClosed"
    />
    <Content>
      <RouterView />
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
