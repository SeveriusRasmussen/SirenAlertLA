package group.sirenalertla.Repository;

import group.sirenalertla.Model.Fire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FireRepository extends JpaRepository<Fire,Long> {
    List<Fire> findByClosedFalse();

    List<Fire> findByTimeLessThanEqual(LocalDateTime dateTime);
}
