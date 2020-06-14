package com.company.webservice.services.user;

import com.company.webservice.domains.User;
import com.company.webservice.exceptions.BadRequestAppException;
import com.company.webservice.exceptions.ResourceName;
import com.company.webservice.exceptions.ResourceNotFoundAppException;
import com.company.webservice.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User newUser) {
        newUser.setEmail(newUser.getEmail().toLowerCase());
        Optional<User> existingUser = userRepository.findByEmail(newUser.getEmail());
        if (existingUser.isPresent()) {
            throw BadRequestAppException.emailTaken(newUser.getEmail());
        }
        return userRepository.save(newUser);
    }

    public User updateUser(User updatedUser) {
        updatedUser.setEmail(updatedUser.getEmail().toLowerCase());
        return userRepository.save(updatedUser);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase());
    }

    public User getExistingUser(long userId) {
        return userRepository.findUserByIdAndDeleted(userId, false)
                .orElseThrow(() -> ResourceNotFoundAppException.resourceNotInDb(ResourceName.USER));
    }

    public User loadUserByUsername(String username) {
        return userRepository.findByEmail(username.toLowerCase())
                .orElseThrow(() -> ResourceNotFoundAppException.resourceNotInDb(ResourceName.USER));
    }
}
