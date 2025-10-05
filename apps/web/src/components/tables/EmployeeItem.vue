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
  border-top: 2px solid #EBF0F6;
}

.employee-item__img-container {
  width: 56px;
  height: 56px;
  overflow: hidden;
  border-radius: 100%;
}

.employee-item__img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.employee-item td {
  padding-block: 14px;
  text-align: center;
  border-radius: 4px;
  vertical-align: middle;
}
</style>
