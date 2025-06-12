package group.sirenalertla.Repository;


import group.sirenalertla.Model.Siren;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SirenRepository extends JpaRepository<Siren, Long> {
}
