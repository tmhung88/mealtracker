package com.mealtracker.services.user;

import com.mealtracker.domains.User;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class UserRegistrationInput {

    @Email
    @Size(min = 5, max = 200)
    private String email;

    @Size(min = 5, max = 200)
    private String fullName;

    @Size(min = 5, max = 100)
    private String password;

    public User toUser() {
        var user = new User();
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPassword(password);
        return user;
    }
}
