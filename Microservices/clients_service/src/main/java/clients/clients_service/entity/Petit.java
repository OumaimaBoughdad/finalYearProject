package clients.clients_service.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Petit {
    @Id
    @GeneratedValue
    private Long idPetit ;
    private String name ;

    public Petit() {}

    public Petit(Long idPetit, String name) {
        this.idPetit = idPetit;
        this.name = name;
    }

    public Long getIdPetit() {
        return idPetit;
    }

    public void setIdPetit(Long idPetit) {
        this.idPetit = idPetit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
