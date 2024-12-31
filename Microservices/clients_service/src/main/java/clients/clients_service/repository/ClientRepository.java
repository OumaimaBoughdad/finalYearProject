

package clients.clients_service.repository;

import clients.clients_service.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    // Additional custom query methods can go here
    Optional<Client> findByCne(String cne);
}

