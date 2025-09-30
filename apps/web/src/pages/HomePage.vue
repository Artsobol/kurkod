<template>
  <div class="home__container">
    <Panel></Panel>
    <div class="home__content">
      <div class="home__first-row">
        <BestChickens></BestChickens>
        <InfoHome></InfoHome>
      </div>
      <div class="home__second-row">
        <Employees></Employees>
        <div class="home__second-row__column">
          <SliderHome></SliderHome>
          <EggsStats></EggsStats>
        </div>
      </div>
    </div>
  </div>

</template>

<script setup>
import Panel from '../components/layouts/Panel.vue'
import BestChickens from "@/sections/BestChickens.vue";
import InfoHome from "@/sections/InfoHome.vue";
import Employees from "@/sections/Employees.vue";
import SliderHome from "@/sections/SliderHome.vue";
import EggsStats from "@/sections/EggsStats.vue";
</script>

<style lang="scss" scoped>
.home {
  &__container {
    display: flex;
    flex-direction: row;
  }

  &__content {
    width: 100%;
    padding: 38px 41px 30px 33px;
    display: flex;
    flex-direction: column;
    gap: 32px;
    @include laptop {
      padding-top: 21px;
    }
  }

  &__first-row {
    display: flex;
    flex-direction: row;
    align-items: flex-end;
    justify-content: space-around;
    gap: 49px;
  }

  &__second-row {
    display: flex;
    flex-direction: row;
    align-items: flex-start;

    justify-content: space-around;
    gap: 37px;
  }

  &__second-row__column {
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    gap: 19px;
  }
}


</style>