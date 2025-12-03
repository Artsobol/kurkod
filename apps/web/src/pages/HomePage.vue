<script setup>
import InfoBlock from "@/components/ui/InfoBlock.vue";
import InfoGraph from "@/components/ui/InfoGraph.vue";
import WorkersTable from "@/components/tables/WorkersTable.vue";
import Button from "@/components/ui/Button.vue";
import {useWorkers} from "@/composables/useWorkers.js";
import Loader from "@/components/ui/Loader.vue";

const {workers, loading} = useWorkers();
</script>

<template>
  <div class="home">
    <div class="info-blocks">
      <div class="page-block page-info-block">
        <InfoBlock
            title="Курицы"
            href="/chickens"
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
            href="/employees"
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
        <InfoGraph title="Производство яиц по цехам" image="/images/graph1.png" href="#"/>
      </div>
      <div class="page-block page-graph-block">
        <InfoGraph title="Производство яиц по цехам" image="/images/graph1.png" href="#"/>
      </div>
      <div class="page-block page-graph-block">
        <InfoGraph title="Производство яиц по цехам" image="/images/graph1.png" href="#"/>
      </div>
    </div>
    <div class="table-employees-block page-block">
      <div class="table-template__header">
        <h2 class="table-template__title">
          Лучшие сотрудники
        </h2>
        <Button
            href="/employees"
            class="table-template__button"
            icon-name="arrow-right"
            :icon-width="28"
            :icon-height="28"
        />
      </div>
      <Loader v-if="loading===true" />
      <WorkersTable
          v-if="loading===false"
          style="margin-block: 8px"
          :headersItem="[
              { key: 'name', label: 'Имя' },
              { key: 'position', label: 'Должность' }
              ]"
          :bodyItems="workers"
          :height-size="5"
      />
    </div>
  </div>
</template>


<style lang="scss">
.page-block {
  background: var(--section-bg);
  border-radius: 8px;
  padding: 12px 19px 17px 17px;
}

.info-blocks {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  margin-bottom: 20px;
}

.graph-blocks {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  margin-bottom: 20px;
}

.page-info-block {
  margin: 0;
  height: 87px;
  background: var(--contrast);

  &:hover {
    transform: scale(1.03);
    transition: 0.15s;
  }

}

.page-graph-block {
  margin: 0;
  background: var(--section-bg);
}

.table-template {
  &__header {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }

  &__title {
    font-size: 16px;
  }
}
</style>