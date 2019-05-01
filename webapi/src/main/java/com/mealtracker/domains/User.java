package com.mealtracker.domains;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Transient
    private String password;

    @Column(name = "encrypted_password", nullable = false)
    private String encryptedPassword;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @Column(name = "role")
    @Enumerated(EnumType.ORDINAL)
    private Role role;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Embedded
    private UserSettings userSettings;

    public List<Privilege> getPrivileges() {
        return role.getPrivileges();
    }
}
