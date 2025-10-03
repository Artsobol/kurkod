<template>
  <div class="home">
    <div class="home__info-blocks">
      <InfoBlock title="Курицы" subTitle="Общее количество" count="1086" href="#"/>
      <InfoBlock title="Яиц в месяц" subTitle="Среднее" count="27000" href="#"/>
      <InfoBlock title="Сотрудники" subTitle="Общее количество" count="200" href="#"/>
      <InfoBlock title="Продуктивность курицы" subTitle="Средняя" count="27" href="#"/>
      <InfoBlock title="Прибыль" subTitle="В месяц" count="28.9KK" href="#"/>
      <InfoBlock title="Курицы" subTitle="Общее количество" count="1086" href="#"/>
    </div>

    <div class="home__info-graphs">
      <InfoGraph
        title="Производство яиц по цехам"
        href="#"
        image="/images/graph1.png"
      />
      <InfoGraph
          title="Количество яиц на работника"
          href="#"
          image="/images/graph2.png"
      />
      <InfoGraph
          title="Динамика прибыли"
          href="#"
          image="/images/graph3.png"
      />
      <InfoGraph
          title="Динамика прозводства"
          href="#"
          image="/images/graph4.png"
      />
    </div>
  </div>

</template>

<script setup>
import Panel from '../components/layouts/Panel.vue'
import BestChickens from "@/sections/BestChickens.vue";
import EggsStats from "@/sections/EggsStats.vue";
import InfoBlock from "@/components/ui/InfoBlock.vue";
import InfoGraph from "@/components/ui/InfoGraph.vue";
</script>

<style lang="scss" scoped>

.lalala {
  display: flex;
  flex-direction: row;
  justify-content: center;
  flex-wrap: wrap;
  column-gap: 38px;
  row-gap: 40px;
}

.tralala {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  flex-wrap: wrap;
  column-gap: 38px;
  row-gap: 40px;
}

.home {
  padding-top: 62px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  max-width: 1459px;
  margin: 0 auto;

  gap: 40px;

  @include desktop-m {
    max-width: 1222px;
  }

  @include desktop {
    max-width: 980px;
  }

  &__info-blocks {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    flex-wrap: wrap;
    column-gap: 38px;
    row-gap: 40px;
  }

  &__info-graphs {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    flex-wrap: wrap;
    column-gap: 38px;
    row-gap: 40px;
  }
}


</style>