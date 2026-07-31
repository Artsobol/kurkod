package io.github.artsobol.kurkod.feature.report.projection;

public interface BreedWorkshopMonthlyProjection {

  Long getWorkshopId();

  Integer getWorkshopNumber();

  Long getBreedId();

  String getBreedName();

  Long getChickensCount();

  Long getEggsTotal();

  java.math.BigDecimal getAvgEggsPerChicken();
}
