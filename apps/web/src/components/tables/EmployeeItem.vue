<template>
  <tr class="employee-item">
    <td>
      <div class="employee-item__img-container">
        <img
            class="employee-item__img"
            :src="photo"
            alt="user photo"
        />
      </div>
    </td>
    <td>{{ name }}</td>
    <td>{{ position }}</td>
    <td>{{ status }}</td>
  </tr>
</template>

<script setup>
defineProps({
  name: String,
  position: String,
  photo: String,
  status: String,
})
</script>

<style scoped lang="scss">
.employee-item {
  border: 1px solid red;
}
.employee-item__img-container {
  width: 72px;
  height: 72px;
  overflow: hidden;
  border-radius: 100%;
}

.employee-item__img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}
</style>
