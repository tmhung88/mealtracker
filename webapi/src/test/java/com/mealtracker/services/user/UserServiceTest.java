package com.mealtracker.services.user;

import com.mealtracker.assertions.AppAssertions;
import com.mealtracker.domains.Role;
import com.mealtracker.domains.User;
import com.mealtracker.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static com.mealtracker.UnitTestError.RESOURCE_NOT_FOUND;
import static com.mealtracker.services.user.UserMatchers.eq;
import static com.mealtracker.services.user.UserMatchers.fields;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Spy
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void addUser_EmailTaken_ExpectException() {
        var newUser = new User();
        newUser.setEmail("EMAIL_taken@abc.com");
        var emailOwner = new User();
        when(userRepository.findByEmail("email_taken@abc.com")).thenReturn(Optional.of(emailOwner));

        AppAssertions.assertThatThrownBy(() -> userService.addUser(newUser)).hasError(40001, "Email email_taken@abc.com is already taken");
    }

    @Test
    public void addUser_ExpectPasswordEncrypted() {
        var newUser = new User();
        newUser.setEmail("good@abc.com");
        newUser.setPassword("strong_password");
        var result = mock(User.class);
        when(passwordEncoder.encode(newUser.getPassword())).thenReturn("encrypted_one");
        when(userRepository.save(eq(fields().encryptedPassword("encrypted_one")))).thenReturn(result);

        assertThat(userService.updateUser(newUser)).isEqualTo(result);
    }


    @Test
    public void addUser_EmailUpperCase_ExpectEmailStoredLowerCase() {
        var newUser = new User();
        newUser.setEmail("ABCDEF@abc.com");
        newUser.setPassword("123456");
        var result = mock(User.class);
        when(userRepository.save(eq(fields().email("abcdef@abc.com")))).thenReturn(result);

        assertThat(userService.updateUser(newUser)).isEqualTo(result);
    }

    @Test
    public void updateUser_NewPasswordAvailable_ExpectNewEncryptedPassworedSaved() {
        var user = new User();
        user.setEmail("abc@gmail.com");
        user.setEncryptedPassword("old_encrypted");
        user.setPassword("i_changed_pass");
        var result = mock(User.class);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("new_encrypted");
        when(userRepository.save(eq(fields().encryptedPassword("new_encrypted")))).thenReturn(result);

        assertThat(userService.updateUser(user)).isEqualTo(result);
    }

    @Test
    public void updateUser_NewPasswordMissing_ExpectOldPasswordRemain() {
        var user = new User();
        user.setEmail("abc@gmail.com");
        user.setEncryptedPassword("old_encrypted");
        user.setPassword(null);
        var result = mock(User.class);

        when(userRepository.save(eq(fields().encryptedPassword("old_encrypted")))).thenReturn(result);

        assertThat(userService.updateUser(user)).isEqualTo(result);
    }

    @Test
    public void updateUser_NewEmailUppercase_ExpectEmailUpdatedWithLowercase() {
        var user = new User();
        user.setEmail("HellO_WORLD@gmail.COM");
        var result = mock(User.class);
        when(userRepository.save(eq(fields().email("hello_world@gmail.com")))).thenReturn(result);

        assertThat(userService.updateUser(user)).isEqualTo(result);
    }

    @Test
    public void findExistingUsers_ExpectSearchForActiveUsers() {
        var roles = Arrays.asList(Role.values());
        var pageable = mock(Pageable.class);
        var result = mock(Page.class);
        when(userRepository.findByDeletedAndRoleIn(false, roles, pageable)).thenReturn(result);

        assertThat(userService.findExistingUsers(roles, pageable)).isEqualTo(result);
    }

    @Test
    public void lookupExistingUsers_ExpectSearchStartWith() {
        var keyword = "Hel";
        var startWith = "hel%";
        var roles = Arrays.asList(Role.REGULAR_USER);
        var pageable = mock(Pageable.class);
        var result = mock(Page.class);
        when(userRepository.lookupExistingUsers(eq(startWith), eq(roles), eq(pageable))).thenReturn(result);

        assertThat(userService.lookupExistingUsers(keyword, roles, pageable)).isEqualTo(result);
    }

    @Test
    public void getExistingUser_NoActiveUser_ExpectException() {
        var userId = 5L;
        when(userRepository.findUserByIdAndDeleted(userId, false)).thenReturn(Optional.empty());

        AppAssertions.assertThatThrownBy(() -> userService.getExistingUser(userId))
                .hasError(RESOURCE_NOT_FOUND.error("user"));
    }

    @Test
    public void getExistingUser_ActiveUser_ExpectUserReturned() {
        var foundUser = new User();
        foundUser.setId(12L);
        when(userRepository.findUserByIdAndDeleted(foundUser.getId(), false)).thenReturn(Optional.of(foundUser));

        assertThat(userService.getExistingUser(foundUser.getId())).isEqualTo(foundUser);
    }


    @Test
    public void loadUserByUsername_EmailNotFound_ExpectAuthenticationException() {
        var unkownEmail = "UnKNown@gmail.com";
        when(userRepository.findByEmail("unknown@gmail.com")).thenReturn(Optional.empty());

        AppAssertions.assertThatThrownBy(() -> userService.loadUserByUsername(unkownEmail))
                .hasError(40102, "There isn't an account for this username");
    }

    @Test
    public void loadUserByUsername_UserDeleted_ExpectAuthenticationException() {
        var deletedUser = new User();
        deletedUser.setEmail("DeletedUser@gmail.com");
        deletedUser.setDeleted(true);

        when(userRepository.findByEmail(deletedUser.getEmail().toLowerCase())).thenReturn(Optional.of(deletedUser));

        AppAssertions.assertThatThrownBy(() -> userService.loadUserByUsername(deletedUser.getEmail()))
                .hasError(40104, "Your account have been deleted. Please contact our supports if you have any question");
    }

    @Test
    public void loadUserByUsername_ExistingEmail_ExpectUserDetailsReturned() {
        var user = new User();
        user.setEmail("user@gmail.com");
        user.setDeleted(false);
        user.setRole(Role.REGULAR_USER);
        user.setEncryptedPassword("32525390idsaf");

        when(userRepository.findByEmail(user.getEmail().toLowerCase())).thenReturn(Optional.of(user));

        var userDetails = userService.loadUserByUsername(user.getEmail());

        assertThat(userDetails).hasFieldOrPropertyWithValue("accountNonExpired", true)
                .hasFieldOrPropertyWithValue("accountNonLocked", true)
                .hasFieldOrPropertyWithValue("accountNonLocked", true)
                .hasFieldOrPropertyWithValue("credentialsNonExpired", true)
                .hasFieldOrPropertyWithValue("enabled", true)
                .hasFieldOrPropertyWithValue("username", user.getEmail())
                .hasFieldOrPropertyWithValue("password", user.getEncryptedPassword());
    }

}
