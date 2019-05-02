package com.mealtracker.api.rest.user;

import com.mealtracker.config.rest.RequestRoleMapping;
import com.mealtracker.payloads.MessageResponse;
import com.mealtracker.payloads.SuccessEnvelop;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mealtracker.config.rest.RequestRoleMapping.RequestRole.USER_MANAGER;


@Secured("USER_MANAGEMENT")
@RestController
@RequestMapping(value = "/v1/users")
@RequestRoleMapping(USER_MANAGER)
public class ManagerUserController {

    @PostMapping
    public SuccessEnvelop<MessageResponse> addUser() {
        return MessageResponse.of("Manager wants to add a new user");
    }

    @GetMapping
    public SuccessEnvelop<MessageResponse> listUsers() {
        return MessageResponse.of("Manager wants to list users");
    }

    @DeleteMapping
    public SuccessEnvelop<MessageResponse> deleteUsers() {
        return MessageResponse.of("Manager wants to delete users");
    }

    @GetMapping(value = "/{id}")
    public SuccessEnvelop<MessageResponse> getUser(@PathVariable Long id) {
        return MessageResponse.of(String.format("Manager wants to get details of user %s", id));
    }

    @PutMapping(value = "/{id}")
    public SuccessEnvelop<MessageResponse> updateUser(@PathVariable Long id) {
        return MessageResponse.of(String.format("Manager wants to update user %s", id));
    }
}
