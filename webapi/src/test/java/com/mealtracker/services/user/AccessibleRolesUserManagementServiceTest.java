package com.mealtracker.services.user;


import com.mealtracker.assertions.AppAssertions;
import com.mealtracker.domains.Role;
import com.mealtracker.domains.User;
import com.mealtracker.domains.UserSettings;
import com.mealtracker.security.CurrentUser;
import com.mealtracker.services.pagination.PageableBuilder;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import com.mealtracker.payloads.Error;
import static com.mealtracker.assertions.AppAssertions.assertThatThrownBy;
import static com.mealtracker.services.user.UserMatchers.eq;
import static com.mealtracker.services.user.UserMatchers.fields;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccessibleRolesUserManagementServiceTest {

    private final List<Role> accessibleRoles = asList(Role.REGULAR_USER, Role.USER_MANAGER);
    private final UserService userService = mock(UserService.class);
    private final PageableBuilder pageableBuilder = mock(PageableBuilder.class);
    private final AccessibleRolesUserManagementService managerService = new AccessibleRolesUserManagementService(
            Role.USER_MANAGER, accessibleRoles, userService, pageableBuilder
    );

    @Test
    public void canUsedBy_ExpectMangerOnly() {
        Assertions.assertThat(managerService.canUsedBy(currentUser(Role.REGULAR_USER))).isFalse();
        Assertions.assertThat(managerService.canUsedBy(currentUser(Role.USER_MANAGER))).isTrue();
        Assertions.assertThat(managerService.canUsedBy(currentUser(Role.ADMIN))).isFalse();
    }

    @Test
    public void addUser_RoleAdmin_ExpectException() {
        var input = manageUserInput();
        input.setRole(Role.ADMIN.name());
        assertThatThrownBy(() -> managerService.addUser(input)).hasError(40001, "You are not allowed to interact with ADMIN");
    }

    @Test
    public void addUser_NormalUser_ExpectUserAddedWithCompleteInfo() {
        var input = manageUserInput();
        input.setEmail("hello_world@gmail.com");
        input.setFullName("Full Name");
        input.setPassword("Nononono");
        input.setDailyCalorieLimit(49);
        input.setRole(Role.USER_MANAGER.name());

        managerService.addUser(input);

        verify(userService).addUser(eq(fields()
                .email(input.getEmail()).fullName(input.getFullName())
                .password(input.getPassword()).role(input.getRole())
                .dailyCalorieLimit(input.getDailyCalorieLimit())
        ));
    }

    @Test
    public void listUsers_ExpectFindingExistingUsersWithinAccessibleRoles() {
        var input = mock(ListUsersInput.class);
        var pageable = mock(Pageable.class);
        when(pageableBuilder.build(input)).thenReturn(pageable);

        managerService.listUsers(input);

        verify(userService).findExistingUsers(accessibleRoles, pageable);
    }

    @Test
    public void lookupUsers_ExpectLookingUpExistingUsersWithinAccessibleRole() {
        var input = mock(ListUsersInput.class);
        var pageable = mock(Pageable.class);
        var keyworkd = "hel";
        when(pageableBuilder.build(input)).thenReturn(pageable);

        managerService.lookupUsers(keyworkd, input);

        verify(userService).lookupExistingUsers(keyworkd, accessibleRoles, pageable);
    }

    @Test
    public void deleteUsers_CurrentUserOnlyDeleteHimself_ExpectException() {
        var currentUser = currentUser(5L);

        assertThatThrownBy(() -> managerService.deleteUsers(deleteInput(currentUser.getId()), currentUser))
                .hasError(40001, "You cannot delete your own account. Please ask your peer to perform");
    }

    @Test
    public void deleteUsers_CurrentUserDeleteMultipleUsers_Himself_ExpectCurrentUserRemoved() {
        var currentUser = currentUser(15L);
        managerService.deleteUsers(deleteInput(1L, 4L, currentUser.getId(), 20L), currentUser);

        verify(userService).softDeleteUsers(asList(1L, 4L, 20L), accessibleRoles);
    }

    @Test
    public void getUser_SuperiorRole_ExpectException() {
        var superiorUser = new User();
        superiorUser.setId(20L);
        superiorUser.setRole(Role.ADMIN);

        when(userService.getExistingUser(superiorUser.getId())).thenReturn(superiorUser);

        assertThatThrownBy(() -> managerService.getUser(superiorUser.getId()))
                .hasError(interactSuperiorRole(Role.ADMIN));
    }

    @Test
    public void getUser_UserWithinAccessibleRoles_ExpectUserDetailsReturn() {
        var accessibleUser = new User();
        accessibleUser.setId(1L);
        accessibleUser.setRole(Role.REGULAR_USER);

        when(userService.getExistingUser(accessibleUser.getId())).thenReturn(accessibleUser);

        Assertions.assertThat(managerService.getUser(accessibleUser.getId())).isEqualTo(accessibleUser);
    }

    @Test
    public void updateUser_UpdatedEmailAlreadyTaken_ExpectException() {
        var input = manageUserInput();
        input.setEmail("used_EMAIL@abc.com");
        var emailOwner = new User();
        emailOwner.setId(5923L);

        when(userService.findByEmail(input.getEmail().toLowerCase())).thenReturn(Optional.of(emailOwner));

        AppAssertions.assertThatThrownBy(() -> managerService.updateUser(21L, input)).hasError(40001, "Email used_email@abc.com is already taken");
    }

    @Test
    public void updateUser_UpdatedUserSuperiorRole_ExpectException() {
        var input = manageUserInput();
        input.setRole(Role.REGULAR_USER.name());
        var superiorUser = new User();
        superiorUser.setId(4L);
        superiorUser.setRole(Role.ADMIN);
        when(userService.findByEmail(input.getEmail().toLowerCase())).thenReturn(Optional.empty());
        when(userService.getExistingUser(superiorUser.getId())).thenReturn(superiorUser);

        assertThatThrownBy(() -> managerService.updateUser(superiorUser.getId(), input))
                .hasError(interactSuperiorRole(superiorUser.getRole()));
    }

    @Test
    public void updateUser_SetUserSuperiorRole_ExpectException() {
        var input = manageUserInput();
        input.setRole(Role.ADMIN.name());
        var updatedUser = new User();
        updatedUser.setId(21L);
        updatedUser.setRole(Role.REGULAR_USER);
        when(userService.findByEmail(input.getEmail().toLowerCase())).thenReturn(Optional.empty());
        when(userService.getExistingUser(updatedUser.getId())).thenReturn(updatedUser);

        assertThatThrownBy(() -> managerService.updateUser(updatedUser.getId(), input))
                .hasError(interactSuperiorRole(input.getRole()));
    }

    @Test
    public void update_ValidInput_ExpectExistingDataMergedNewInfo() {
        var input = manageUserInput();
        input.setFullName("Kite Shark");
        input.setPassword("hheehe");
        input.setEmail("emai@abc.com");
        input.setRole(Role.USER_MANAGER.name());
        input.setDailyCalorieLimit(400);

        var updatedUser = new User();
        updatedUser.setId(21L);
        updatedUser.setRole(Role.REGULAR_USER);
        updatedUser.setUserSettings(new UserSettings());
        when(userService.getExistingUser(updatedUser.getId())).thenReturn(updatedUser);

        managerService.updateUser(updatedUser.getId(), input);

        verify(userService).updateUser(eq(fields()
                .id(updatedUser.getId()).fullName(input.getFullName()).password(input.getPassword())
                .email(input.getEmail()).role(input.getRole()).dailyCalorieLimit(input.getDailyCalorieLimit())
        ));

    }

    CurrentUser currentUser(Role role) {
        var user = mock(CurrentUser.class);
        when(user.getRole()).thenReturn(role);
        return user;
    }

    CurrentUser currentUser(long id) {
        var user = mock(CurrentUser.class);
        when(user.getId()).thenReturn(id);
        return user;
    }

    ManageUserInput manageUserInput() {
        var input = new ManageUserInput();
        input.setEmail("decent_one@gmail.com");
        input.setDailyCalorieLimit(0);
        return input;
    }

    DeleteUsersInput deleteInput(Long... userIds) {
        var input = new DeleteUsersInput();
        input.setIds(asList(userIds));
        return input;
    }

    private Error interactSuperiorRole(Role role) {
        var errorMessage = "You are not allowed to interact with " + role.name();
        return Error.of(40001, errorMessage);
    }

}
