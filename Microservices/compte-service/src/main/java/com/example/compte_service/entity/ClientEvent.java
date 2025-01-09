package com.example.compte_service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientEvent {
    private int clientId;
    private String eventType;
    private Client clientData;

    // Getters and Setters

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Client getClientData() {
        return clientData;
    }

    public void setClientData(Client clientData) {
        this.clientData = clientData;
    }

    @Override
    public String toString() {
        return "ClientEvent{" +
                "clientId=" + clientId +
                ", eventType='" + eventType + '\'' +
                ", clientData=" + clientData +
                '}';
    }
}

