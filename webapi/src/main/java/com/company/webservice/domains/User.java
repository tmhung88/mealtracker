package com.company.webservice.domains;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    public boolean isEnabled() {
        return !deleted;
    }
}
