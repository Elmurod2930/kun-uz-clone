package com.example.kunuz.entity;

import com.example.kunuz.enums.GeneralStatus;
import com.example.kunuz.enums.ProfileRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "profile")
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "password")
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private GeneralStatus status;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private ProfileRole role;
    @Column(name = "visible")
    private Boolean visible = Boolean.TRUE;
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attach_id")
    private AttachEntity attach;
    @Column(name = "prt_id")
    private Integer prtId;
}
