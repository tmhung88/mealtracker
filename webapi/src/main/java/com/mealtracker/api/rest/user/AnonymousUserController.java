package com.mealtracker.api.rest.user;

import com.mealtracker.domains.User;
import com.mealtracker.exceptions.ResourceName;
import com.mealtracker.exceptions.ResourceNotFoundAppException;
import com.mealtracker.payloads.MessageResponse;
import com.mealtracker.payloads.PublicUserInfoResponse;
import com.mealtracker.payloads.SuccessEnvelop;
import com.mealtracker.services.user.UserRegistrationInput;
import com.mealtracker.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/users")
public class AnonymousUserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public SuccessEnvelop<MessageResponse> registerUser(@Valid @RequestBody UserRegistrationInput registrationInput) {
        userService.registerUser(registrationInput);
        return MessageResponse.of("User registered successfully");
    }

    @GetMapping(params = "email")
    public SuccessEnvelop<PublicUserInfoResponse> findUserByEmail(@RequestParam String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> ResourceNotFoundAppException.resourceNotInDb(ResourceName.USER));
        return PublicUserInfoResponse.of(user);
    }
}
