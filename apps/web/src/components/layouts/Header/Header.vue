<template>
  <header class="header">
    <nav class="nav">
      <h1 class="logo">🌐 My Website</h1>
      <ul class="menu">
        <li><a href="#">Главная</a></li>
        <li><a href="#">О нас</a></li>
        <li><a href="#">Контакты</a></li>
      </ul>
    </nav>
  </header>
</template>

<script setup>
// Здесь JS-кода нет, но позже можно добавить логику (например, переключение темы)
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
