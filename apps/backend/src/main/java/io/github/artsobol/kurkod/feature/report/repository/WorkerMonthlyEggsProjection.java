package io.github.artsobol.kurkod.feature.report.repository;

public interface WorkerMonthlyEggsProjection {
    Long getWorkerId();
    String getFirstName();
    String getLastName();
    Long getEggsPerMonth();
}
