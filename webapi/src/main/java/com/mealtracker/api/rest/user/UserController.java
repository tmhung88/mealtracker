package com.mealtracker.api.rest.user;

import com.mealtracker.config.rest.AuthenticatedMapping;
import com.mealtracker.payloads.MessageResponse;
import com.mealtracker.payloads.MetaSuccessEnvelop;
import com.mealtracker.payloads.PaginationMeta;
import com.mealtracker.payloads.SuccessEnvelop;
import com.mealtracker.payloads.user.LookupUserInfoResponse;
import com.mealtracker.payloads.user.ManageUserInfoResponse;
import com.mealtracker.security.CurrentUser;
import com.mealtracker.services.user.DeleteUsersInput;
import com.mealtracker.services.user.ListUsersInput;
import com.mealtracker.services.user.ManageUserInput;
import com.mealtracker.services.user.UserManagementServiceResolver;
import com.mealtracker.validation.OnAdd;
import com.mealtracker.validation.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Secured("USER_MANAGEMENT")
@RestController
@RequestMapping(value = "/v1/users")
@AuthenticatedMapping
public class UserController {

    @Autowired
    private UserManagementServiceResolver serviceResolver;

    @PostMapping
    public SuccessEnvelop<MessageResponse> addUser(@Validated(OnAdd.class) @Valid @RequestBody ManageUserInput input, CurrentUser currentUser) {
        serviceResolver.resolve(currentUser).addUser(input);
        return MessageResponse.of("User added successfully");
    }

    @GetMapping
    public MetaSuccessEnvelop<List<ManageUserInfoResponse>, PaginationMeta> listUsers(@Valid ListUsersInput input, CurrentUser currentUser) {
        var userPage = serviceResolver.resolve(currentUser).listUsers(input);
        return ManageUserInfoResponse.envelop(userPage);
    }

    @GetMapping(params = "keyword")
    public MetaSuccessEnvelop<List<LookupUserInfoResponse>, PaginationMeta> lookupUsers(@RequestParam String keyword,
                                                                                        @Valid ListUsersInput input,
                                                                                        CurrentUser currentUser) {
        var userPage = serviceResolver.resolve(currentUser).lookupUsers(keyword, input);
        return LookupUserInfoResponse.envelop(userPage);
    }

    @DeleteMapping
    public SuccessEnvelop<MessageResponse> deleteUsers(@Valid @RequestBody DeleteUsersInput input, CurrentUser currentUser) {
        serviceResolver.resolve(currentUser).deleteUsers(input, currentUser);
        return MessageResponse.of("Users deleted successfully");
    }

    @GetMapping("/{userId}")
    public SuccessEnvelop<ManageUserInfoResponse> getUser(@PathVariable Long userId, CurrentUser currentUser) {
        var user = serviceResolver.resolve(currentUser).getUser(userId);
        return ManageUserInfoResponse.envelop(user);
    }


    @PutMapping("/{userId}")
    public SuccessEnvelop<MessageResponse> updateUser(@PathVariable long userId,
                                                      @Validated(OnUpdate.class) @Valid
                                                      @RequestBody ManageUserInput input,
                                                      CurrentUser currentUser) {
        serviceResolver.resolve(currentUser).updateUser(userId, input);
        return MessageResponse.of("User updated successfully");
    }
}
