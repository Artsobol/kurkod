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
        <InfoGraph title="Производство яиц по цехам" image="/images/graph1.png" href="#"/>
      </div>
      <div class="page-block page-graph-block">
        <InfoGraph title="Производство яиц по цехам" image="/images/graph1.png" href="#"/>
      </div>
      <div class="page-block page-graph-block">
        <InfoGraph title="Производство яиц по цехам" image="/images/graph1.png" href="#"/>
      </div>
    </div>
  </div>

</template>

<script setup>
import InfoBlock from "@/components/ui/InfoBlock.vue";
import InfoGraph from "@/components/ui/InfoGraph.vue";
</script>

<style lang="scss" scoped>
.page-block {
  background: var(--color-white);
  border-radius: 6px;
  box-shadow: 0 4px 4px 0 rgba(61, 55, 136, 0.25);
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
  padding: 12px 19px 17px 17px;
  background: var(--color-purple-contrast);
  &:hover {
    transform: scale(1.03);
    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  }
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.page-graph-block {
  margin: 0;
  padding: 12px 19px 17px 17px;
  background: var(--color-white);
}


</style>