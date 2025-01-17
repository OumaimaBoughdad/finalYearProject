package org.example.credit_microservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Entity
@Table(name = "loan_predictions")
public class LoanPredictionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int person_age;
    private int person_income;
    private String person_home_ownership;
    private double person_emp_length;
    private String loan_intent;
    private String loan_grade;
    private double loan_amnt;
    private double loan_int_rate;
    private double loan_percent_income;



    @Column(nullable = false, columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String cb_person_default_on_file = "N";

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private int cb_person_cred_hist_length = 0;

    // Getters and Setters

    public LoanPredictionRequest(Long id,int personAge, int personIncome, String personHomeOwnership, double personEmpLength,
                      String loanIntent, String loanGrade, double loanAmnt, double loanIntRate, double loanPercentIncome,
                      String cbPersonDefaultOnFile, int cbPersonCredHistLength) {
        this.id=id;
        this.person_age = personAge;
        this.person_income = personIncome;
        this.person_home_ownership = personHomeOwnership;
        this.person_emp_length = personEmpLength;
        this.loan_intent = loanIntent;
        this.loan_grade = loanGrade;
        this.loan_amnt = loanAmnt;
        this.loan_int_rate = loanIntRate;
        this.loan_percent_income = loanPercentIncome;
        this.cb_person_default_on_file = cbPersonDefaultOnFile;
        this.cb_person_cred_hist_length = cbPersonCredHistLength;
    }

    public LoanPredictionRequest() {

    }


    public int getPerson_age() {
        return person_age;
    }

    public void setPerson_age(int person_age) {
        this.person_age = person_age;
    }

    public int getPerson_income() {
        return person_income;
    }

    public void setPerson_income(int person_income) {
        this.person_income = person_income;
    }

    public String getPerson_home_ownership() {
        return person_home_ownership;
    }

    public void setPerson_home_ownership(String person_home_ownership) {
        this.person_home_ownership = person_home_ownership;
    }

    public double getPerson_emp_length() {
        return person_emp_length;
    }

    public void setPerson_emp_length(double person_emp_length) {
        this.person_emp_length = person_emp_length;
    }

    public String getLoan_intent() {
        return loan_intent;
    }

    public void setLoan_intent(String loan_intent) {
        this.loan_intent = loan_intent;
    }

    public String getLoan_grade() {
        return loan_grade;
    }

    public void setLoan_grade(String loan_grade) {
        this.loan_grade = loan_grade;
    }

    public double getLoan_amnt() {
        return loan_amnt;
    }

    public void setLoan_amnt(double loan_amnt) {
        this.loan_amnt = loan_amnt;
    }

    public double getLoan_int_rate() {
        return loan_int_rate;
    }

    public void setLoan_int_rate(double loan_int_rate) {
        this.loan_int_rate = loan_int_rate;
    }

    public double getLoan_percent_income() {
        return loan_percent_income;
    }

    public void setLoan_percent_income(double loan_percent_income) {
        this.loan_percent_income = loan_percent_income;
    }

    public String getCb_person_default_on_file() {
        return cb_person_default_on_file;
    }

    public void setCb_person_default_on_file(String cb_person_default_on_file) {
        this.cb_person_default_on_file = cb_person_default_on_file;
    }

    public int getCb_person_cred_hist_length() {
        return cb_person_cred_hist_length;
    }

    public void setCb_person_cred_hist_length(int cb_person_cred_hist_length) {
        this.cb_person_cred_hist_length = cb_person_cred_hist_length;
    }
}
