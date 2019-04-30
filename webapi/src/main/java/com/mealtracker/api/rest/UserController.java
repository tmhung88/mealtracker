package com.mealtracker.api.rest;

import com.mealtracker.domains.User;
import com.mealtracker.exceptions.ResourceNotFoundException;
import com.mealtracker.payloads.MessageResponse;
import com.mealtracker.payloads.PublicUserInfoResponse;
import com.mealtracker.payloads.SuccessEnvelop;
import com.mealtracker.payloads.UserListResponse;
import com.mealtracker.payloads.UserRegistrationRequest;
import com.mealtracker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public SuccessEnvelop<MessageResponse> registerUser(@Valid @RequestBody UserRegistrationRequest registrationRequest) {
        userService.registerUser(registrationRequest.toUser());
        return MessageResponse.of("User registered successfully");
    }

    @GetMapping(params = "email")
    public SuccessEnvelop<PublicUserInfoResponse> findUserByEmail(@RequestParam String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with the given email"));
        return PublicUserInfoResponse.of(user);
    }


    @Secured({"USER_MANAGEMENT"})
    @GetMapping
    public SuccessEnvelop<UserListResponse> listUsers() {
        return UserListResponse.of();
    }
}
