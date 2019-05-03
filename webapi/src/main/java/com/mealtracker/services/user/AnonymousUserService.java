package com.mealtracker.services.user;

import com.mealtracker.domains.Role;
import com.mealtracker.domains.User;
import com.mealtracker.exceptions.ResourceName;
import com.mealtracker.exceptions.ResourceNotFoundAppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnonymousUserService {
    @Autowired
    private UserService userService;

    public User registerUser(RegisterUserInput registrationInput) {
        var newUser = registrationInput.toUser();
        newUser.setRole(Role.REGULAR_USER);
        return userService.addUser(newUser);
    }

    public User getByEmail(String email) {
        return userService.findByEmail(email)
                .orElseThrow(() -> ResourceNotFoundAppException.resourceNotInDb(ResourceName.USER));
    }
}
