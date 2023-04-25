package com.example.kunuz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_history")
@Getter
@Setter
public class EmailHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "TEXT", name = "massage")
    private String massage;
    @Column(name = "email")
    private String email;
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
}
