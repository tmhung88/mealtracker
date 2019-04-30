package com.mealtracker.api.rest;

import com.mealtracker.domains.User;
import com.mealtracker.exceptions.ResourceNotFoundException;
import com.mealtracker.payloads.PublicUserInfoResponse;
import com.mealtracker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping(params = "email")
    public Object findByEmail(@RequestParam String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> ResourceNotFoundException.notFound("User not found with the given email"));
        return PublicUserInfoResponse.of(user);
    }

}
