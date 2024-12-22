package com.example.client_service.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="client")
@Data

public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_client", nullable = false,updatable = false)
    public long idClient;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = true)
    private String phoneNumber;

    @Column(name = "job", nullable = true)
    private String job;


}
