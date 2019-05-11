package com.mealtracker.api.rest.user;

import com.mealtracker.config.rest.RequestRoleMapping;
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
import com.mealtracker.services.user.manager.ManagerUserService;
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

import static com.mealtracker.config.rest.RequestRoleMapping.RequestRole.USER_MANAGER;


@Secured("USER_MANAGEMENT")
@RestController
@RequestMapping(value = "/v1/users")
@RequestRoleMapping(USER_MANAGER)
public class ManagerUserController {

    @Autowired
    private ManagerUserService managerUserService;

    @PostMapping
    public SuccessEnvelop<MessageResponse> addUser(@Validated(OnAdd.class) @Valid @RequestBody ManageUserInput input) {
        managerUserService.addUser(input);
        return MessageResponse.of("User added successfully");
    }

    @GetMapping
    public MetaSuccessEnvelop<List<ManageUserInfoResponse>, PaginationMeta> listUsers(@Valid ListUsersInput listUsersInput) {
        var userPage = managerUserService.listUsers(listUsersInput);
        return ManageUserInfoResponse.envelop(userPage);
    }

    @GetMapping(params = "keyword")
    public MetaSuccessEnvelop<List<LookupUserInfoResponse>, PaginationMeta> lookupUsers(@RequestParam String keyword,
                                                                                        @Valid ListUsersInput input) {
        var userPage = managerUserService.lookupUsers(keyword, input);
        return LookupUserInfoResponse.envelop(userPage);
    }

    @DeleteMapping
    public SuccessEnvelop<MessageResponse> deleteUsers(@Valid @RequestBody DeleteUsersInput input, CurrentUser currentUser) {
        managerUserService.deleteUsers(input, currentUser);
        return MessageResponse.of("Users deleted successfully");
    }

    @GetMapping("/{userId}")
    public SuccessEnvelop<ManageUserInfoResponse> getUser(@PathVariable Long userId) {
        var user = managerUserService.getUser(userId);
        return ManageUserInfoResponse.envelop(user);
    }

    @PutMapping("/{userId}")
    public SuccessEnvelop<MessageResponse> updateUser(@PathVariable Long userId,
                                                      @Validated(OnUpdate.class) @Valid @RequestBody ManageUserInput input) {
        managerUserService.updateUser(userId, input);
        return MessageResponse.of("User updated successfully");
    }
}
