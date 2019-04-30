package com.mealtracker.services;

import com.mealtracker.domains.Role;
import com.mealtracker.domains.User;
import com.mealtracker.exceptions.BadRequestException;
import com.mealtracker.repositories.UserRepository;
import com.mealtracker.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        Optional<User> existingUser = findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new BadRequestException(String.format("Email %s is already taken", user.getEmail()));
        }
        user.setRole(Role.REGULAR_USER);
        user.setEnabled(true);
        user.setEncryptedPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDetails loadUserById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        return new UserPrincipal(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new UserPrincipal(user);
    }
}
