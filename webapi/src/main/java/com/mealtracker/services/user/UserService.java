package com.mealtracker.services.user;

import com.mealtracker.domains.Role;
import com.mealtracker.domains.User;
import com.mealtracker.domains.UserSettings;
import com.mealtracker.exceptions.BadRequestAppException;
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
            throw BadRequestAppException.emailTaken(user.getEmail());
        }
        user.setRole(Role.REGULAR_USER);
        user.setEnabled(true);
        user.setEncryptedPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserSettings(new UserSettings());
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findActiveUser(long userId) {
        // TODO: Implement it
        return Optional.empty();
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new UserPrincipal(user);
    }
}
