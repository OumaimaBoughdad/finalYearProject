package projet.client_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projet.client_service.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    // Custom query methods can be added here if necessary
}
