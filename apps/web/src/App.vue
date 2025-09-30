<script setup>
import { useRoute } from 'vue-router'
import Header from './components/layouts/Header/Header.vue'

const route = useRoute()
</script>

<template>
  <div>
    <Header :title="route.meta.title" />
    <router-view />
  </div>
</template>

<style>
</style>
