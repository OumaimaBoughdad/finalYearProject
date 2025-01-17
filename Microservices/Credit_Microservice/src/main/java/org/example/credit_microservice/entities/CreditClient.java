package org.example.credit_microservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "Credit_clients")
public class CreditClient {

    @Id
    private Long cni; // Primary key

    private String name;
    private int personAge;
    private double personIncome;
    private String personHomeOwnership;
    private double personEmpLength;
    private String cbPersonDefaultOnFile;
    private int cbPersonCredHistLength;
    @OneToMany(mappedBy = "client") // One client can have many credits
    private List<Credit> credits;

    // No-args constructor
    public CreditClient() {
    }

    // All-args constructor
    public CreditClient(Long cni, String name, int personAge, double personIncome, String personHomeOwnership,
                  double personEmpLength, String cbPersonDefaultOnFile, int cbPersonCredHistLength) {
        this.cni = cni;
        this.name = name;
        this.personAge = personAge;
        this.personIncome = personIncome;
        this.personHomeOwnership = personHomeOwnership;
        this.personEmpLength = personEmpLength;
        this.cbPersonDefaultOnFile = cbPersonDefaultOnFile;
        this.cbPersonCredHistLength = cbPersonCredHistLength;
    }

    // Getters and setters
    public Long getCni() {
        return cni;
    }

    public void setCni(Long cni) {
        this.cni = cni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPersonAge() {
        return personAge;
    }

    public void setPersonAge(int personAge) {
        this.personAge = personAge;
    }

    public double getPersonIncome() {
        return personIncome;
    }

    public void setPersonIncome(double personIncome) {
        this.personIncome = personIncome;
    }

    public String getPersonHomeOwnership() {
        return personHomeOwnership;
    }

    public void setPersonHomeOwnership(String personHomeOwnership) {
        this.personHomeOwnership = personHomeOwnership;
    }

    public double getPersonEmpLength() {
        return personEmpLength;
    }

    public void setPersonEmpLength(double personEmpLength) {
        this.personEmpLength = personEmpLength;
    }

    public String getCbPersonDefaultOnFile() {
        return cbPersonDefaultOnFile;
    }

    public void setCbPersonDefaultOnFile(String cbPersonDefaultOnFile) {
        this.cbPersonDefaultOnFile = cbPersonDefaultOnFile;
    }

    public int getCbPersonCredHistLength() {
        return cbPersonCredHistLength;
    }

    public void setCbPersonCredHistLength(int cbPersonCredHistLength) {
        this.cbPersonCredHistLength = cbPersonCredHistLength;
    }
}
