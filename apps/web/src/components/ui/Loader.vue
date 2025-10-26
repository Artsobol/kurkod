<script setup>
</script>

<template>
  <div class="loader-wrapper">
    <div class="loader"></div>
  </div>
</template>


<style scoped lang="scss">
.loader-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  animation: fadeIn 0.5s ease-in-out;
}

.loader {
  width: 50px;
  height: 50px;
  border: 4px solid rgba(#726DAF, 0.2);
  border-top-color: var(--contrast);
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 12px;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

@keyframes fadeIn {
  from { opacity: 0; transform: scale(0.9); }
  to { opacity: 1; transform: scale(1); }
}
</style>