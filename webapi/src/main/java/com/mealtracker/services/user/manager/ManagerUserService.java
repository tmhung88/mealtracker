package com.mealtracker.services.user.manager;

import com.mealtracker.domains.Role;
import com.mealtracker.domains.User;
import com.mealtracker.exceptions.BadRequestAppException;
import com.mealtracker.security.CurrentUser;
import com.mealtracker.services.pagination.PageableBuilder;
import com.mealtracker.services.user.DeleteUsersInput;
import com.mealtracker.services.user.ListUsersInput;
import com.mealtracker.services.user.ManageUserInput;
import com.mealtracker.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Transactional
@Service
public class ManagerUserService {
    private static final List<Role> MANAGER_ACCESSIBLE_ROLES = Arrays.asList(Role.REGULAR_USER, Role.USER_MANAGER);

    @Autowired
    private UserService userService;

    @Autowired
    private PageableBuilder pageableBuilder;

    public void addUser(ManageUserInput input) {
        var newUser = input.toUser();
        var canUseRole = MANAGER_ACCESSIBLE_ROLES.contains(newUser.getRole());
        if (!canUseRole) {
            throw BadRequestAppException.setSuperiorRole(Role.ADMIN);
        }
        userService.addUser(newUser);
    }

    public Page<User> listUsers(ListUsersInput input) {
        var pageable = pageableBuilder.build(input);
        return userService.findExistingUsers(MANAGER_ACCESSIBLE_ROLES, pageable);
    }

    public Page<User> lookupUsers(String keyword, ListUsersInput input) {
        var pageable = pageableBuilder.build(input);
        return userService.lookupExistingUsers(keyword, MANAGER_ACCESSIBLE_ROLES, pageable);
    }

    public void deleteUsers(DeleteUsersInput input, CurrentUser currentUser) {
        var isDeletingHimSelf = input.getIds().size() == 1 && input.getIds().contains(currentUser.getId());
        if (isDeletingHimSelf) {
            throw BadRequestAppException.deleteYourself();
        }

        Predicate<Long> notCurrentUser = id -> !id.equals(currentUser.getId());
        var userIds = input.getIds().stream()
                .filter(notCurrentUser)
                .distinct().collect(Collectors.toList());

        userService.softDeleteUsers(userIds, MANAGER_ACCESSIBLE_ROLES);
    }

    public User getUser(long userId) {
        var user = userService.getExistingUser(userId);
        var canSeeRole = MANAGER_ACCESSIBLE_ROLES.contains(user.getRole());
        if (!canSeeRole) {
            throw BadRequestAppException.setSuperiorRole(Role.ADMIN);
        }
        return user;
    }

    public User updateUser(long userId, ManageUserInput input) {
        User existingUser = userService.getExistingUser(userId);
        var canPerformOnUser = MANAGER_ACCESSIBLE_ROLES.contains(existingUser.getRole());
        var canUpdateToRole = MANAGER_ACCESSIBLE_ROLES.contains(input.getRole());
        if (!canPerformOnUser || !canUpdateToRole) {
            throw BadRequestAppException.setSuperiorRole(Role.ADMIN);
        }

        input.merge(existingUser);
        return userService.updateUser(existingUser);
    }
}
