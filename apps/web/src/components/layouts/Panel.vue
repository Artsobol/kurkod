<template>
  <div class="panel">
    <ul class="panel__list">
      <li class="panel__list-item-title">
        <PanelItem href="#" title="Меню" isMainTitle></PanelItem>
      </li>
      <li v-for="(item, index) in panelMenuItems" :key="index">
        <PanelItem
            :title="item.label"
            :href="item.href"
        />
      </li>
      <li class="panel__list-item-title">
        <PanelItem href="#" title="Курицы" isMainTitle></PanelItem>
      </li>
      <li v-for="(item, index) in panelChickensItems" :key="index">
        <PanelItem
            :title="item.label"
            :href="item.href"
        />
      </li>
      <li class="panel__list-item-title">
        <PanelItem href="#" title="Уход" isMainTitle></PanelItem>
      </li>
      <li v-for="(item, index) in panelCareItems" :key="index">
        <PanelItem
            :title="item.label"
            :href="item.href"
        />
      </li>
    </ul>
  </div>
</template>

<script setup>
import PanelItem from './PanelItem.vue'

const panelMenuItems = [
  {
    label: "Главная",
    href: "#"
  },
  {
    label: "Сотрудники",
    href: "#"
  },
  {
    label: "Производство",
    href: "#"
  },
  {
    label: "Заказы",
    href: "#"
  },
  {
    label: "Логистика",
    href: "#"
  },
  {
    label: "Отчеты",
    href: "#"
  }
]
const panelChickensItems = [
  {
    label: "Курицы",
    href: "#"
  },
  {
    label: "Петухи",
    href: "#"
  },
  {
    label: "Цыплята",
    href: "#"
  }
]
const panelCareItems = [
  {
    label: "Корм",
    href: "#"
  },
  {
    label: "Диеты",
    href: "#"
  },
  {
    label: "Загоны",
    href: "#"
  }
]

</script>

<style lang="scss" scoped>
.panel {
  position: fixed;
  width: 350px;
  left: 0;
  top: 79px;
  background-color: var(--color-white);
  height: calc(100vh - 79px);
  box-shadow: 4px 0 4px 0 rgba(61, 55, 136, 0.25);
  padding-block: 40px;
  display: flex;
  flex-direction: column;
  align-items: center;
  z-index: 9;
}

.panel__list {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 30px;
  overflow-y: auto;
  padding: 0 40px;

  &::-webkit-scrollbar {
    width: 8px;
  }

  &::-webkit-scrollbar-track {
    background: transparent;
  }

  &::-webkit-scrollbar-thumb {
    background-color: var(--color-purple-contrast);
    border-radius: 4px;
  }

  &::-webkit-scrollbar-thumb:hover {
    background-color: #575196;
  }
}
</style>