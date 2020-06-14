package com.company.webservice.repositories;

import com.company.webservice.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findUserByIdAndDeleted(long userId, boolean deleted);
}
