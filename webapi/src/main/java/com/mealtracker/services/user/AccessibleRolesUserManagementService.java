package com.mealtracker.services.user;


import com.mealtracker.domains.Role;
import com.mealtracker.domains.User;
import com.mealtracker.exceptions.BadRequestAppException;
import com.mealtracker.security.CurrentUser;
import com.mealtracker.services.pagination.PageableBuilder;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Transactional
public class AccessibleRolesUserManagementService implements UserManagementService {
    private final Role usedBy;
    private final List<Role> accessibleRoles;
    private final UserService userService;
    private final PageableBuilder pageableBuilder;

    public AccessibleRolesUserManagementService(Role usedBy, List<Role> accessibleRoles, UserService userService, PageableBuilder pageableBuilder) {
        this.usedBy = usedBy;
        this.accessibleRoles = accessibleRoles;
        this.userService = userService;
        this.pageableBuilder = pageableBuilder;
    }

    @Override
    public boolean canUsedBy(CurrentUser currentUser) {
        return usedBy == currentUser.getRole();
    }

    @Override
    public void addUser(ManageUserInput input) {
        var newUser = input.toUser();
        var canUseRole = accessibleRoles.contains(newUser.getRole());
        if (!canUseRole) {
            throw BadRequestAppException.setSuperiorRole(newUser.getRole());
        }
        userService.addUser(newUser);
    }

    @Override
    public Page<User> listUsers(ListUsersInput input) {
        var pageable = pageableBuilder.build(input);
        return userService.findExistingUsers(accessibleRoles, pageable);
    }

    @Override
    public Page<User> lookupUsers(String keyword, ListUsersInput input) {
        var pageable = pageableBuilder.build(input);
        return userService.lookupExistingUsers(keyword, accessibleRoles, pageable);
    }

    @Override
    public void deleteUsers(DeleteUsersInput input, CurrentUser currentUser) {
        var isDeletingHimSelf = input.getIds().size() == 1 && input.getIds().contains(currentUser.getId());
        if (isDeletingHimSelf) {
            throw BadRequestAppException.deleteYourself();
        }

        Predicate<Long> notCurrentUser = id -> !id.equals(currentUser.getId());
        var userIds = input.getIds().stream()
                .filter(notCurrentUser)
                .distinct().collect(Collectors.toList());

        userService.softDeleteUsers(userIds, accessibleRoles);
    }

    @Override
    public User getUser(long userId) {
        var user = userService.getExistingUser(userId);
        var canSeeRole = accessibleRoles.contains(user.getRole());
        if (!canSeeRole) {
            throw BadRequestAppException.setSuperiorRole(user.getRole());
        }
        return user;
    }

    @Override
    public User updateUser(long userId, ManageUserInput input) {
        var email = input.getEmail().toLowerCase();
        var emailOwner = userService.findByEmail(email);
        boolean isSameOwner = emailOwner.map(owner -> owner.getId() == userId).orElse(true);
        if (!isSameOwner) {
            throw BadRequestAppException.emailTaken(email);
        }

        User existingUser = userService.getExistingUser(userId);
        var canPerformOnUser = accessibleRoles.contains(existingUser.getRole());
        var canUpdateToRole = accessibleRoles.contains(input.getRole());
        if (!canPerformOnUser || !canUpdateToRole) {
            throw BadRequestAppException.setSuperiorRole(Role.ADMIN);
        }

        input.merge(existingUser);
        return userService.updateUser(existingUser);
    }
}
