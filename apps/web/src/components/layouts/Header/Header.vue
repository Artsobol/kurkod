<template>
  <header class="header">
    <h1 class="header__title">

    </h1>
  </header>
</template>

<script setup>

// Объявляем проп title
defineProps({
  title: {
    type: String,
    required: true
  }
})
</script>

<style scoped>
.header {
  background-color: #42b983;
}

.nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  color: white;
  margin: 0;
}

.menu {
  list-style: none;
  display: flex;
  gap: 20px;
}

.menu a {
  color: white;
  text-decoration: none;
}

.menu a:hover {
  text-decoration: underline;
}
</style>
