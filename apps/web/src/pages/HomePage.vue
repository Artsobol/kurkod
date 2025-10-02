<template>
  <div class="home">
    <div class="home__info-blocks">
      <InfoBlock title="Курицы" subTitle="Общее количество" count="1086" href="#"/>
      <InfoBlock title="Курицы" subTitle="Общее количество" count="1086" href="#"/>
      <InfoBlock title="Курицы" subTitle="Общее количество" count="1086" href="#"/>
      <InfoBlock title="Курицы" subTitle="Общее количество" count="1086" href="#"/>
      <InfoBlock title="Курицы" subTitle="Общее количество" count="1086" href="#"/>
      <InfoBlock title="Курицы" subTitle="Общее количество" count="1086" href="#"/>
    </div>
    <div class="home__graph-blocks">

    </div>

<!--    <div class="home__content">-->
<!--      <div class="home__first-row">-->
<!--        <BestChickens></BestChickens>-->
<!--      </div>-->
<!--      <div class="home__second-row">-->
<!--        <Employees></Employees>-->
<!--        <div class="home__second-row__column">-->
<!--          <SliderHome></SliderHome>-->
<!--          <EggsStats></EggsStats>-->
<!--        </div>-->
<!--      </div>-->
<!--    </div>-->
  </div>

</template>

<script setup>
import Panel from '../components/layouts/Panel.vue'
import BestChickens from "@/sections/BestChickens.vue";
import InfoHome from "@/sections/InfoHome.vue";
import Employees from "@/sections/Employees.vue";
import SliderHome from "@/sections/SliderHome.vue";
import EggsStats from "@/sections/EggsStats.vue";
import InfoBlock from "@/components/ui/InfoBlock.vue";
</script>

<style lang="scss" scoped>
.home {
  padding-top: 62px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 40px;
  &__info-blocks {
    display: grid;
    grid-template-columns: 1fr 1fr 1fr;
    gap: 28px;
  }
}


</style>