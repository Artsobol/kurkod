package io.github.artsobol.kurkod.web.domain.dismissal.repository;

import io.github.artsobol.kurkod.web.domain.dismissal.model.entity.Dismissal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DismissalRepository extends JpaRepository<Dismissal, Integer> {

    Optional<Dismissal> findDismissalByWorker_Id(int id);

    Optional<Dismissal> findDismissalByWorker_IdAndWhoDismiss_Id(int workerId, int whoDismissedId);

    List<Dismissal> findAllByWorker_Id(int id);

    List<Dismissal> findAllByWhoDismiss_Id(int id);

    boolean existsByWorker_IdAndWhoDismiss_Id(int workerId, int whoDismissedId);
}
