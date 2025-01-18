package org.example.credit_microservice.DTO;


public class LoanStatusByDefaultDTO {
    private String defaultOnFile; // "Y" or "N"
    private long noDefaultCount;  // Count of loan_status = 0
    private long defaultCount;    // Count of loan_status = 1

    // Constructor, Getters, and Setters
    public LoanStatusByDefaultDTO(String defaultOnFile, long noDefaultCount, long defaultCount) {
        this.defaultOnFile = defaultOnFile;
        this.noDefaultCount = noDefaultCount;
        this.defaultCount = defaultCount;
    }

    public String getDefaultOnFile() {
        return defaultOnFile;
    }

    public void setDefaultOnFile(String defaultOnFile) {
        this.defaultOnFile = defaultOnFile;
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
