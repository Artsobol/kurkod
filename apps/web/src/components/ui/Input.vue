<template class="input-container">
  <form
      class="input__form"
      action="/search" method="get">
    <img
        class="input__form-icon"
        src="/ui-icons/search.svg"
        alt=""
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
  height: 54px;
  max-width: 245px;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 0 12px;
  border-radius: 8px;
  box-shadow: var(--shadow);
  overflow: hidden;

  &-icon {
    width: 24px;
    height: 24px;
    flex-shrink: 0;
  }

  &-input {
    flex: 1;
    height: 100%;

    &::placeholder {
      font-weight: 400;
      font-size: 16px;
      color: #686590;
    }
  }
}
</style>