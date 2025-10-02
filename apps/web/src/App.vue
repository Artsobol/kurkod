<script setup>
import { useRoute } from 'vue-router'
import Header from './components/layouts/Header.vue'
import Panel from "@/components/layouts/Panel.vue";
import HomePage from "@/pages/HomePage.vue";
const route = useRoute()
</script>

<template>
  <div class="layouts">
    <Header />
    <Panel></Panel>
  </div>
  <div class="content">
    <HomePage />
  </div>
</template>

<style lang="scss" scoped>
.content {
  margin-top: 79px;
  margin-left: 350px;
}
</style>
