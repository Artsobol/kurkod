<template>
  <aside
      :class="['sidebar', {closed: isClosed}]">
      <ul>
        <li>🏠 <span v-if="!isClosed">Главная</span></li>
        <li>📂 <span v-if="!isClosed">Проекты</span></li>
        <li>⚙️ <span v-if="!isClosed">Настройки</span></li>
        <li>❓ <span v-if="!isClosed">Помощь</span></li>
      </ul>
  </aside>
</template>

<script setup>
defineProps({
  isClosed: Boolean,
})
</script>

<style scoped>
.sidebar {
  width: 250px;
  background: red;
  color: black;
  transition: width 0.3s ease;
  flex-shrink: 0;
  overflow: hidden;
}

.sidebar.closed {
  width: 60px;
}

.sidebar ui li {
  padding: 12px 20px;
  cursor: pointer;
  transition: background 1s ease;
}
</style>