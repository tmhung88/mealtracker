package com.company.webservice.services.user;

import com.company.webservice.exceptions.AuthenticationAppException;
import com.company.webservice.exceptions.BadRequestAppException;
import com.company.webservice.exceptions.ResourceName;
import com.company.webservice.exceptions.ResourceNotFoundAppException;
import com.company.webservice.repositories.UserRepository;
import com.company.webservice.domains.Role;
import com.company.webservice.domains.User;
import com.company.webservice.security.UserPrincipal;
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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
