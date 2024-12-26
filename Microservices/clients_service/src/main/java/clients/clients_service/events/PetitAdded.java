package clients.clients_service.events;

public class PetitAdded {

    private Long petitId;

    public Long getPetitId() {
        return petitId;
    }

    public void setPetitId(Long petitId) {
        this.petitId = petitId;
    }

    public PetitAdded(Long petitId) {
        this.petitId = petitId;
    }
}
