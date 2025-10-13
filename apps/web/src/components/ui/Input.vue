<template class="input-container">


  <form
      class="input__form"
      action="/search" method="get">
    <Icon
        name="search"
        width="24"
        height="24"
    />
    <input
        class="input__form-input"
        type="text"
        name="query"
        :placeholder="labelInput"/>
  </form>
</template>
<script setup>
defineProps({
  labelInput: {
    type: String,
    default: 'Найти',
  }
})
</script>

<style lang="scss" scoped>
.input__form {
  background: var(--color-white);
  color: var(--color-text-light);
  height: 54px;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 0 16px;
  border-radius: 8px;
  box-shadow: var(--shadow);
  overflow: hidden;

  &-input {
    flex: 1;
    height: 100%;
    max-width: 180px;

    &::placeholder {
      font-weight: 400;
      font-size: 16px;
      color: #686590;
    }
  }
}
</style>