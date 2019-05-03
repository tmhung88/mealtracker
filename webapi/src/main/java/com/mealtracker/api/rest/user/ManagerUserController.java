package com.mealtracker.api.rest.user;

import com.mealtracker.config.rest.RequestRoleMapping;
import com.mealtracker.payloads.MessageResponse;
import com.mealtracker.payloads.MetaSuccessEnvelop;
import com.mealtracker.payloads.PaginationMeta;
import com.mealtracker.payloads.SuccessEnvelop;
import com.mealtracker.payloads.user.ManageUserResponse;
import com.mealtracker.security.CurrentUser;
import com.mealtracker.services.user.DeleteUsersInput;
import com.mealtracker.services.user.ListUsersInput;
import com.mealtracker.services.user.ManageUserInput;
import com.mealtracker.services.user.manager.ManagerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static com.mealtracker.config.rest.RequestRoleMapping.RequestRole.USER_MANAGER;


@Secured("USER_MANAGEMENT")
@RestController
@RequestMapping(value = "/v1/users")
@RequestRoleMapping(USER_MANAGER)
public class ManagerUserController {

    @Autowired
    private ManagerUserService managerUserService;

    @PostMapping
    public SuccessEnvelop<MessageResponse> addUser(@Valid @RequestBody ManageUserInput input) {
        managerUserService.addUser(input);
        return MessageResponse.of("User added successfully");
    }

    @GetMapping
    public MetaSuccessEnvelop<List<ManageUserResponse>, PaginationMeta> listUsers(@Valid ListUsersInput listUsersInput) {
        var userPage = managerUserService.listUsers(listUsersInput);
        return ManageUserResponse.envelop(userPage);
    }

    @DeleteMapping
    public SuccessEnvelop<MessageResponse> deleteUsers(@Valid @RequestBody DeleteUsersInput input, CurrentUser currentUser) {
        managerUserService.deleteUsers(input, currentUser);
        return MessageResponse.of("Users deleted successfully");
    }

    @GetMapping(value = "/{userId}")
    public SuccessEnvelop<ManageUserResponse> getUser(@PathVariable Long userId) {
        var user = managerUserService.getUser(userId);
        return ManageUserResponse.envelop(user);
    }

    @PutMapping(value = "/{userId}")
    public SuccessEnvelop<MessageResponse> updateUser(@PathVariable Long userId,
                                                      @Valid @RequestBody ManageUserInput input) {
        managerUserService.updateUser(userId, input);
        return MessageResponse.of("User updated successfully");
    }
}
