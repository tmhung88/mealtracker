package com.company.webservice.services.user;

import com.company.webservice.domains.User;
import com.company.webservice.exceptions.ResourceName;
import com.company.webservice.exceptions.ResourceNotFoundAppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublicUserService {

    private final UserService userService;

    @Autowired
    public PublicUserService(UserService userService) {
        this.userService = userService;
    }

    public User registerUser(RegisterUserInput registrationInput) {
        var newUser = registrationInput.toUser();
        return userService.addUser(newUser);
    }

    public User getByEmail(String email) {
        return userService.findByEmail(email)
                .orElseThrow(() -> ResourceNotFoundAppException.resourceNotInDb(ResourceName.USER));
    }
}
