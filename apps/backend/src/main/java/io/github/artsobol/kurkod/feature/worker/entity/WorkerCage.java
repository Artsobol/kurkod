package io.github.artsobol.kurkod.feature.worker.entity;

import io.github.artsobol.kurkod.feature.cage.entity.Cage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "worker_cage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkerCage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "worker_id", nullable = false)
  private Worker worker;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "cage_id", nullable = false)
  private Cage cage;
}
