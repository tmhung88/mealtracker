package com.mealtracker.api.rest;

import com.mealtracker.domains.User;
import com.mealtracker.payloads.MessageResponse;
import com.mealtracker.payloads.SuccessEnvelop;
import com.mealtracker.payloads.user.PublicUserInfoResponse;
import com.mealtracker.services.user.PublicUserService;
import com.mealtracker.services.user.RegisterUserInput;
import com.mealtracker.validation.OnAdd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/users")
public class PublicUserController {

    private final PublicUserService publicUserService;

    @Autowired
    public PublicUserController(PublicUserService publicUserService) {
        this.publicUserService = publicUserService;
    }

    @PostMapping
    public SuccessEnvelop<MessageResponse> registerUser(@Validated(OnAdd.class) @Valid
                                                        @RequestBody RegisterUserInput registrationInput) {
        publicUserService.registerUser(registrationInput);
        return MessageResponse.of("User registered successfully");
    }

    @GetMapping(params = "email")
    public SuccessEnvelop<PublicUserInfoResponse> getUser(@RequestParam String email) {
        User user = publicUserService.getByEmail(email);
        return PublicUserInfoResponse.of(user);
    }
}
