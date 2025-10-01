<template>
  <div class="chicken-table">
    <table class="chicken-table__table">
      <thead>
      <tr>
        <th>
          Photo
          <img
              src="/ui-icons/table-photo.svg"
              alt=""
          />
        </th>
        <th>
          Name
          <img
              src="/ui-icons/table-name.svg"
              alt=""
          />
        </th>
        <th>
          Breed
          <img
              src="/ui-icons/table-breed.svg"
              alt=""
          />
        </th>
        <th>
          Weight
          <img
              src="/ui-icons/table-weight.svg"
              alt=""
          />
        </th>
        <th>
          Age
          <img
              src="/ui-icons/table-age.svg"
              alt=""
          />
        </th>
        <th>
          Gender
          <img
              src="/ui-icons/table-gender.svg"
              alt=""
          />
        </th>
        <th>
          Eggs/Mo
          <img
              src="/ui-icons/table-eggs.svg"
              alt=""
          />
        </th>
      </tr>
      </thead>
      <tbody v-for="(chicken, index) in chickens.slice(0, 3)">
      <tr :key="index">
        <td>
          <span class="chicken-table__table__image-container">
            <img
                :src="chicken.photo"
                alt=""
            >
          </span>
        </td>
        <td>
          <span>
            {{ chicken.name }}
          </span>
        </td>
        <td>
          <span>
            {{ chicken.breed }}
          </span>
        </td>
        <td>
          <span>
            {{ chicken.weight }}
          </span>
        </td>
        <td>
          <span>
            {{ chicken.age }}
          </span>
        </td>
        <td>
          <span>
            {{ chicken.gender }}
          </span>
        </td>
        <td>
          <span v-if="chicken.eggsPerMonth">
            {{ chicken.eggsPerMonth }}
          </span>
          <span v-else>
            -
          </span>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import chickens from "@/constants/CHICKENS_DATA.json";

defineProps({
  size: {
    type: String,
    required: true
  }
})
</script>

<style scoped lang="scss">
.chicken-table__table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
  table-layout: fixed;
  font-weight: 500;
  font-size: 18px;
  color: var(--color-brown-dark);

  @include desktop {
  }

  &__image-container {
    width: 64px;
    height: 64px;
    overflow: hidden;
    display: flex;
    justify-content: center;
    align-items: center;
    background: var(--color-sand-dark);
    border-radius: 100%;
    margin: 0 auto;
  }

  &__image-container img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.chicken-table__table th,
.chicken-table__table td {
  font-weight: 500;
  padding: 10px;
  text-align: center;
  border-radius: 4px;
  width: calc(100% / 7);

  @include desktop {
    //padding: 6px 6px;
  }
}

.chicken-table__table td {
  font-family: var(--font-family-montserrat);
  font-weight: 400;
  font-size: 18px;
}

</style>