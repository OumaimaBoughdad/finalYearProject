package org.example.credit_microservice.DTO;


public class LoanStatusByCreditHistoryDTO {
    private String creditHistoryLength;
    private long noDefaultCount;
    private long defaultCount;

    // Constructor, Getters, and Setters
    public LoanStatusByCreditHistoryDTO(String creditHistoryLength, long noDefaultCount, long defaultCount) {
        this.creditHistoryLength = creditHistoryLength;
        this.noDefaultCount = noDefaultCount;
        this.defaultCount = defaultCount;
    }

    public String getCreditHistoryLength() {
        return creditHistoryLength;
    }

    public void setCreditHistoryLength(String creditHistoryLength) {
        this.creditHistoryLength = creditHistoryLength;
    }

    public long getNoDefaultCount() {
        return noDefaultCount;
    }

    public void setNoDefaultCount(long noDefaultCount) {
        this.noDefaultCount = noDefaultCount;
    }

    public long getDefaultCount() {
        return defaultCount;
    }

    public void setDefaultCount(long defaultCount) {
        this.defaultCount = defaultCount;
    }
}