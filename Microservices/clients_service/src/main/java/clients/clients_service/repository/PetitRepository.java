package clients.clients_service.repository;

import clients.clients_service.entity.Petit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetitRepository extends JpaRepository<Petit,Long> {
}
