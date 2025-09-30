<template>
  <div class="panel">
    <ul class="panel__list">
      <li v-for="(item, index) in panelItems" :key="index">
        <PanelItem
            :title="item.label"
            :image="item.image"
            :href="item.href"
        />
      </li>
    </ul>
  </div>
</template>

<script setup>
import PanelItem from '../Panel/PanelItem.vue'

const panelItems = [
  {
    label: "Home",
    image: "home",
    href: "#"
  },
  {
    label: "Chickens",
    image: "chicken",
    href: "#"
  },
  {
    label: "Chick",
    image: "chick",
    href: "#"
  },
  {
    label: "Breeds",
    image: "rooster",
    href: "#"
  },
  {
    label: "Diets",
    image: "diet",
    href: "#"
  },
  {
    label: "Cages",
    image: "cage",
    href: "#"
  },
  {
    label: "Employees",
    image: "employee",
    href: "#"
  },
  {
    label: "Reports",
    image: "report",
    href: "#"
  }
]
</script>

<style lang="scss" scoped>
.panel {
  width: 250px;
  height: 100vh;
  background-color: var(--color-white);
  padding-top: 28px;
  padding-left: 39px;

}
.panel__list {
  display: flex;
  flex-direction: column;
  gap: 26px;


}
</style>