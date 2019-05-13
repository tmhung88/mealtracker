package com.mealtracker.services.user;

import com.mealtracker.domains.Role;
import com.mealtracker.domains.User;
import com.mealtracker.exceptions.AuthenticationAppException;
import com.mealtracker.exceptions.BadRequestAppException;
import com.mealtracker.exceptions.ResourceName;
import com.mealtracker.exceptions.ResourceNotFoundAppException;
import com.mealtracker.repositories.UserRepository;
import com.mealtracker.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {
    private static final String START_WITH_TEMPLATE = "%s%%";
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User addUser(User newUser) {
        newUser.setEmail(newUser.getEmail().toLowerCase());
        Optional<User> existingUser = userRepository.findByEmail(newUser.getEmail());
        if (existingUser.isPresent()) {
            throw BadRequestAppException.emailTaken(newUser.getEmail());
        }
        newUser.setEncryptedPassword(passwordEncoder.encode(newUser.getPassword()));

        return userRepository.save(newUser);
    }

    public User updateUser(User updatedUser) {
        updatedUser.setEmail(updatedUser.getEmail().toLowerCase());
        boolean isPasswordChanged = updatedUser.getPassword() != null;
        if (isPasswordChanged) {
            updatedUser.setEncryptedPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        return userRepository.save(updatedUser);
    }

    public void softDeleteUsers(List<Long> userIds, List<Role> roles) {
        userRepository.softDelete(userIds, roles);
    }

    public Page<User> findExistingUsers(List<Role> includedRoles, Pageable pageable) {
        return userRepository.findByDeletedAndRoleIn(false, includedRoles, pageable);
    }

    public Page<User> lookupExistingUsers(String keyword, List<Role> includedRoles, Pageable pageable) {
        var startWith = String.format(START_WITH_TEMPLATE, keyword.toLowerCase());
        return userRepository.lookupExistingUsers(startWith, includedRoles, pageable);
    }


    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase());
    }

    public User getExistingUser(long userId) {
        return userRepository.findUserByIdAndDeleted(userId, false)
                .orElseThrow(() -> ResourceNotFoundAppException.resourceNotInDb(ResourceName.USER));
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = userRepository.findByEmail(username.toLowerCase())
                .orElseThrow(AuthenticationAppException::usernameNotFound);
        if (user.isDeleted()) {
            throw AuthenticationAppException.accountDeleted();
        }
        return UserPrincipal.allDetails(user);
    }
}
