package com.mealtracker.api.rest.user;

import com.mealtracker.domains.User;
import com.mealtracker.payloads.MessageResponse;
import com.mealtracker.payloads.SuccessEnvelop;
import com.mealtracker.payloads.user.PublicUserInfoResponse;
import com.mealtracker.services.user.AnonymousUserService;
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
public class AnonymousUserController {

    @Autowired
    private AnonymousUserService anonymousUserService;

    @PostMapping
    public SuccessEnvelop<MessageResponse> registerUser(@Validated(value = OnAdd.class) @Valid
                                                        @RequestBody RegisterUserInput registrationInput) {
        anonymousUserService.registerUser(registrationInput);
        return MessageResponse.of("User registered successfully");
    }

    @GetMapping(params = "email")
    public SuccessEnvelop<PublicUserInfoResponse> findUserByEmail(@RequestParam String email) {
        User user = anonymousUserService.getByEmail(email);
        return PublicUserInfoResponse.of(user);
    }
}
