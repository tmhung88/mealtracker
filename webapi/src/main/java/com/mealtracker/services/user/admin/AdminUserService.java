package com.mealtracker.services.user.admin;

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

@Service
@Transactional
public class AdminUserService {
    private static final List<Role> ADMIN_ACCESSIBLE_ROLES = Arrays.asList(Role.values());

    @Autowired
    private UserService userService;

    @Autowired
    private PageableBuilder pageableBuilder;

    public User addUser(ManageUserInput input) {
        return userService.addUser(input.toUser());
    }

    public Page<User> listUsers(ListUsersInput input) {
        var pageable = pageableBuilder.build(input);
        return userService.findExistingUsers(ADMIN_ACCESSIBLE_ROLES, pageable);
    }

    public Page<User> lookupUsers(String keyword, ListUsersInput input) {
        var pageable = pageableBuilder.build(input);
        return userService.lookupExistingUsers(keyword, ADMIN_ACCESSIBLE_ROLES, pageable);
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

        userService.softDeleteUsers(userIds, ADMIN_ACCESSIBLE_ROLES);
    }

    public User getUser(long userId) {
        return userService.getExistingUser(userId);
    }

    public User updateUser(long userId, ManageUserInput input) {
        User existingUser = userService.getExistingUser(userId);
        input.merge(existingUser);
        return userService.updateUser(existingUser);
    }


}
