<template>
  <main class="content">
    <slot></slot>
  </main>
</template>

<style scoped>
.content {
  flex:1;
  background-color: white;
  transition: margin-left 0.3s ease, width 0.3s ease;
  padding: 20px;
}
</style>