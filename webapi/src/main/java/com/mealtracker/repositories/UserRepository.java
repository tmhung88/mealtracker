package com.mealtracker.repositories;

import com.mealtracker.domains.Role;
import com.mealtracker.domains.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findUserByIdAndDeleted(long userId, boolean deleted);

    Page<User> findByDeletedAndRoleIn(boolean deleted, List<Role> includedRoles, Pageable pageable);

    @Modifying
    @Query("UPDATE User user SET user.deleted = false where user.id IN :userIds AND user.role IN :roles")
    void softDelete(@Param("userIds") List<Long> userIds, @Param("roles") List<Role> roles);

    @Query("SELECT user FROM User user WHERE user.deleted = false AND user.role IN :roles AND " +
            "user.email LIKE :startWith")
    Page<User> lookupExistingUsers(@Param("startWith") String startWith,
                                   @Param("roles") List<Role> roles, Pageable pageable);

}
