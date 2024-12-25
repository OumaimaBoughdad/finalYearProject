package clients.clients_service.events;




public class ClientPlacedEvent {

    private Long clientId;

    public ClientPlacedEvent(Long clientId) {
        this.clientId = clientId;
    }


    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
