package com.juandlr.spring_boot_testing.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employees")
@Setter @Getter @AllArgsConstructor @NoArgsConstructor @Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;
}
