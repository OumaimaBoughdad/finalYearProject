package org.example.credit_microservice.DTO;


public class LoanStatusByCreditHistoryDTO {
    private String creditHistoryLength; // Binned categories: "0-2", "3-5", "6-10", "10+"
    private long noDefaultCount;        // Count of loan_status = 0
    private long defaultCount;          // Count of loan_status = 1

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