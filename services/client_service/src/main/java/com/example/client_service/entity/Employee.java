package com.example.client_service.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name = "employees")
@Data

public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_employee", nullable = false, updatable = false)
    private long idEmployee;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Client> clients;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = true)
    private String phoneNumber;

    @Column (nullable = false)
    private String role;

    @Column (nullable = false)
    private String password;



}
