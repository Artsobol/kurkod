<template>
  <div class="user page-block">
    <div class="user__img-container">
      <img
          class="user__img"
          :src="photo"
          alt="User photo"
      />
    </div>
    <div class="user__container">
      <div class="user__info">
        <h2>Основная информация</h2>
        <h3 class="h2">ФИО</h3>
        <span>
        {{ props.surname }}
        {{ props.name }}
        {{ props.patronymic }}
      </span>
        <h3 class="h2">Должность</h3>
        <span>
        {{ props.position }}
      </span>

        <div style="display: flex; flex-direction: row; gap: 16px; align-items: center;">
          <h3 class="h2">Заработная плата</h3>
          <Button
              :icon-name="isHiddenSalary ? 'show' : 'hide'"
              :icon-width="19"
              :icon-height="19"
              @click="toggleSalary"
          />
        </div>
        <span>
          {{ isHiddenSalary ? maskedSalary : salary + ' ₽'}}
      </span>
      </div>
      <div class="user__info" style="min-width: 20%;">
        <h2>Контакты</h2>
        <div style="display: flex; flex-direction: row; gap: 40px; align-items: center;">
          <h3 class="h2">Почта</h3>
          <Button
              :icon-name="isHiddenEmail ? 'show' : 'hide'"
              :icon-width="19"
              :icon-height="19"
              @click="toggleEmail"
          />
        </div>
        <span style="max-width: 250px; white-space: normal; word-wrap: break-word; overflow-wrap: anywhere;">
          {{ isHiddenEmail ? maskedEmail : email }}
      </span>
        <div style="display: flex; flex-direction: row; gap: 16px; align-items: center;">
          <h3 class="h2">Телефон</h3>
          <Button
              :icon-name="isHiddenPhone ? 'show' : 'hide'"
              :icon-width="20"
              :icon-height="20"
              @click="togglePhone"
          />
        </div>
        <span>
          {{ isHiddenPhone ? maskedPhone : phone }}
      </span>
      </div>
      <div class="user__info">
        <h2>Документы</h2>
        <h3 class="h2">Паспорт</h3>
        <span class="text-ellipsis" style="max-width: 200px">
        sdjsdakhjshkdajkasdhjkhjsdaksdahdsdsddsj
      </span>
        <h3 class="h2">Договор</h3>
        <span class="text-ellipsis" style="max-width: 200px">
        sdjsdakhjshkdajkasdhjkhjsdaksdahdsdsddsj
      </span>
      </div>
    </div>
  </div>
</template>
<script setup>
import {computed, ref} from "vue";
import Button from "@/components/ui/Button.vue";

const props = defineProps({
  photo: {
    type: String,
    required: false,
  },
  surname: {
    type: String,
    required: true,
  },
  name: {
    type: String,
    required: true,
  },
  patronymic: {
    type: String,
    required: true,
  },
  position: {
    type: String,
    required: true,
  },
  salary: {
    type: Number,
    required: true,
  },
  phone: {
    type: String,
    required: true,
  },
  email: {
    type: String,
    required: true,
  },
  status: {
    type: String,
    required: true,
  }
})

const phone = ref(props.phone)
const email = ref(props.email)
const salary = ref(props.salary)

const isHiddenPhone = ref(true)
const isHiddenEmail = ref(true)
const isHiddenSalary = ref(true)

const maskedPhone = computed(() => {
  return phone.value.replace(/\d/g, '*')
})

const maskedEmail = computed(() => {
  return email.value.replace(/[^@]/g, '*')
})

const maskedSalary = computed(() => {
  return salary.value.toString().replace(/\d/g, '*')
})

function togglePhone() {
  isHiddenPhone.value = !isHiddenPhone.value
}

function toggleEmail() {
  isHiddenEmail.value = !isHiddenEmail.value
}

function toggleSalary() {
  isHiddenSalary.value = !isHiddenSalary.value
}
</script>

<style lang="scss" scoped>
.user {
  display: flex;
  flex-direction: row;

  &__container {
    width: 70%;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin-inline: auto;
    gap: 64px
  }
  &__info {
    display: flex;
    flex-direction: column;
    gap: 12px;
  }

  &__img-container {
    width: 14vw;
    height: 14vw;
    overflow: hidden;
    border-radius: 8px;
  }

  &__img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    display: block;
  }
}

</style>