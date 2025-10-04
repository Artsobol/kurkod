<script setup>
import { useRoute } from 'vue-router'
import Header from './components/layouts/Header.vue'
import Panel from "@/components/layouts/Panel.vue";
import HomePage from "@/pages/HomePage.vue";
import Sidebar from "@/components/layouts/Sidebar.vue";
import Content from "@/components/layouts/Content.vue";
const route = useRoute()

import {ref} from "vue";
import InfoBlock from "@/components/ui/InfoBlock.vue";
const isClosed = ref(false);

function toggleSidebar() {
  isClosed.value = !isClosed.value;
}
</script>

<template>
  <Header @toggle="toggleSidebar"/>
  <div class="wrapper">
    <Sidebar
      :isClosed="isClosed"
    />

    <Content>
      <div class="info-blocks">
        <div class="page-block page-info-block">
          <InfoBlock
              title="Курицы"
              href="#"
              subTitle="Общее количество"
              count="1086"
          />
        </div>
        <div class="page-block page-info-block">
          <InfoBlock
              title="Яиц в месяц"
              href="#"
              subTitle="Среднее"
              count="27000"
          />
        </div>
        <div class="page-block page-info-block">
          <InfoBlock
              title="Сотрудники"
              href="#"
              subTitle="Общее количество"
              count="200"
          />
        </div>
        <div class="page-block page-info-block">
          <InfoBlock
              title="Продуктивность курицы"
              href="#"
              subTitle="Средняя"
              count="27"
          />
        </div>
        <div class="page-block page-info-block">
          <InfoBlock
              title="Прибыль"
              href="#"
              subTitle="В месяц"
              count="28.9KK"
          />
        </div>
        <div class="page-block page-info-block">
          <InfoBlock
              title="Курицы"
              href="#"
              subTitle="Общее количество"
              count="1086"
          />
        </div>
      </div>
      <div class="graph-blocks">
        <div class="page-block page-graph-block">
          <h2 class="info-graph__title">
            Производство яиц по цехам
          </h2>
          <a class="info-graph__button" href="#">
            <img
                src="/ui-icons/arrow-right.svg"
                alt=""
            />
          </a>
          <div class="info-graph__image-container">
            <img
                src="/images/graph1.png"
                alt=""
            />
          </div>
        </div>
        <div class="page-block page-graph-block">
          <h2 class="info-graph__title">
            Производство яиц по цехам
          </h2>
          <a class="info-graph__button" href="#">
            <img
                src="/ui-icons/arrow-right.svg"
                alt=""
            />
          </a>
          <div class="info-graph__image-container">
            <img
                src="/images/graph1.png"
                alt=""
            />
          </div>
        </div>
        <div class="page-block page-graph-block">
          <h2 class="info-graph__title">
            Производство яиц по цехам
          </h2>
          <a class="info-graph__button" href="#">
            <img
                src="/ui-icons/arrow-right.svg"
                alt=""
            />
          </a>
          <div class="info-graph__image-container">
            <img
                src="/images/graph1.png"
                alt=""
            />
          </div>
        </div>
        <div class="page-block page-graph-block">
          <h2 class="info-graph__title">
            Производство яиц по цехам
          </h2>
          <a class="info-graph__button" href="#">
            <img
                src="/ui-icons/arrow-right.svg"
                alt=""
            />
          </a>
          <div class="info-graph__image-container">
            <img
                src="/images/graph1.png"
                alt=""
            />
          </div>
        </div>

      </div>
    </Content>

  </div>
</template>

<style lang="scss" scoped>
.wrapper {
  display: flex;
  width: 100%;
  overflow-x: hidden;
  margin-top: 79px;
  height: calc(100vh - 79px);
}

.page-block {
  background: var(--color-white);
  border-radius: 6px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}

.info-blocks {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  margin-bottom: 20px;
}

.graph-blocks {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24px;
  margin-bottom: 20px;
}

.page-info-block {
  margin: 0;
  height: 87px;
  padding: 12px 19px 17px 17px;
  background: var(--color-purple-contrast);
  &:hover {
    transform: scale(1.03);
    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  }
  transition: transform 0.2s ease, box-shadow 0.2s ease;

}
</style>
