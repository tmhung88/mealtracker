package com.company.webservice.api.rest;

import com.company.webservice.domains.User;
import com.company.webservice.payloads.MessageResponse;
import com.company.webservice.payloads.SuccessEnvelop;
import com.company.webservice.payloads.user.PublicUserInfoResponse;
import com.company.webservice.services.user.PublicUserService;
import com.company.webservice.services.user.RegisterUserInput;
import com.company.webservice.validation.OnAdd;
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
