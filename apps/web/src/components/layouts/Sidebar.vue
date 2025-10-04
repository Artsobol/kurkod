<template>
  <aside :class="['sidebar', { closed: isClosed }]">
    <ul>
      <li>
        🏠 <span class="link-text" :class="[{ hidden: isClosed }, 'link-text-contrast']">Меню</span>
      </li>
      <li>
        📂 <span class="link-text" :class="{ hidden: isClosed }">Главная</span>
      </li>
      <li>
        ⚙️ <span class="link-text" :class="{ hidden: isClosed }">Сотрудники</span>
      </li>
      <li>
        ❓ <span class="link-text" :class="{ hidden: isClosed }">Производство</span>
      </li>
      <li>
        ❓ <span class="link-text" :class="{ hidden: isClosed }">Заказы</span>
      </li>
      <li>
        ❓ <span class="link-text" :class="{ hidden: isClosed }">Логистика</span>
      </li>
      <li>
        ❓ <span class="link-text" :class="{ hidden: isClosed }">Отчеты</span>
      </li>
      <li>
        ❓ <span class="link-text" :class="[{ hidden: isClosed }, 'link-text-contrast']">Курицы</span>
      </li>
      <li>
        ❓ <span class="link-text" :class="{ hidden: isClosed }">Курицы</span>
      </li>
      <li>
        ❓ <span class="link-text" :class="{ hidden: isClosed }">Петухи</span>
      </li>
      <li>
        ❓ <span class="link-text" :class="{ hidden: isClosed }">Цыплята</span>
      </li>
      <li>
        ❓ <span class="link-text" :class="[{ hidden: isClosed }, 'link-text-contrast']">Уход</span>
      </li>
      <li>
        ❓ <span class="link-text" :class="{ hidden: isClosed }">Корм</span>
      </li>
      <li>
        ❓ <span class="link-text" :class="{ hidden: isClosed }">Диеты</span>
      </li>
      <li>
        ❓ <span class="link-text" :class="{ hidden: isClosed }">Загоны</span>
      </li>
    </ul>
  </aside>
</template>

<script setup>
defineProps({
  isClosed: Boolean,
});
</script>

<style scoped>
.sidebar {
  width: 250px;
  background: var(--color-white, #fff);
  color: var(--color-text-light);
  font-weight: 500;
  font-size: 14px;
  transition: width 0.3s ease;
  flex-shrink: 0;
  overflow: hidden;
  padding-top: 20px;

  border-right: 1px solid #9A97C5FF;
}

.sidebar.closed {
  width: 60px;
}

.sidebar ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.sidebar ul li {
  padding: 10px 20px;
  cursor: pointer;
  display: flex;
  align-items: center;
}

.link-text {
  display: inline-block;
  white-space: nowrap;
  overflow: hidden;
  max-width: 200px;
  opacity: 1;
  margin-left: 8px;
  transition: max-width 0.3s ease, opacity 0.3s ease, margin-left 0.3s ease;
  padding-left: 10px;
}

.link-text-contrast {
  color: var(--color-text);
  font-weight: 600;
}

.link-text.hidden {
  max-width: 0;
  opacity: 0;
  margin-left: 0;
}
</style>
