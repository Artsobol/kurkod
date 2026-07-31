package io.github.artsobol.kurkod.feature.report.entity;


import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "report_chicken_egg_stats")
public class ChickenEggStatsView {

    @Id
    @Column(name = "chicken_id")
    private Long chickenId;

    @Column(name = "chicken_name")
    private String chickenName;

    @Column(name = "breed_id")
    private Long breedId;

    @Column(name = "breed_name")
    private String breedName;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "eggs_count")
    private Long eggsCount;
}