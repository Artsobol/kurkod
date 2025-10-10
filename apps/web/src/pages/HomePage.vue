<template>
  <div class="home">
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
            specific-button="arrow-right"
        />
      </div>
      <TableTemplate
          :bodyItems="['Имя', 'Должность', 'Статус']"
          :headersItem="['', 'Имя', 'Должность', 'Статус']"
          stylesPack="table-employees" />
    </div>
  </div>
</template>

<script setup>
import InfoBlock from "@/components/ui/InfoBlock.vue";
import InfoGraph from "@/components/ui/InfoGraph.vue";
import TableTemplate from "@/components/tables/TableTemplate.vue";
import Button from "@/components/ui/Button.vue";
</script>

<style lang="scss">
.page-block {
  background: var(--section-bg);
  border-radius: 6px;
  box-shadow: 0 4px 4px 0 rgba(61, 55, 136, 0.25);
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
    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  }
  transition: transform 0.2s ease, box-shadow 0.2s ease;
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