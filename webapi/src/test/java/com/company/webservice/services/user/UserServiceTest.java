package com.company.webservice.services.user;

import com.company.webservice.assertions.AppAssertions;
import com.company.webservice.domains.User;
import com.company.webservice.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static com.company.webservice.UnitTestError.RESOURCE_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Spy
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void addUser_EmailTaken_ExpectException() {
        var newUser = new User();
        newUser.setEmail("EMAIL_taken@abc.com");
        var emailOwner = new User();
        when(userRepository.findByEmail("email_taken@abc.com")).thenReturn(Optional.of(emailOwner));

        AppAssertions.assertThatThrownBy(() -> userService.addUser(newUser)).hasError(40001, "Email email_taken@abc.com is already taken");
    }

    @Test
    public void addUser_EmailUpperCase_ExpectEmailStoredLowerCase() {
        var newUser = new User();
        newUser.setEmail("ABCDEF@abc.com");
        var result = mock(User.class);
        when(userRepository.save(UserMatchers.eq(UserMatchers.fields().email("abcdef@abc.com")))).thenReturn(result);

        assertThat(userService.updateUser(newUser)).isEqualTo(result);
    }

    @Test
    public void updateUser_NewEmailUppercase_ExpectEmailUpdatedWithLowercase() {
        var user = new User();
        user.setEmail("HellO_WORLD@gmail.COM");
        var result = mock(User.class);
        when(userRepository.save(UserMatchers.eq(UserMatchers.fields().email("hello_world@gmail.com")))).thenReturn(result);

        assertThat(userService.updateUser(user)).isEqualTo(result);
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
                .hasError(40401, "The given user does not exist");
    }

    @Test
    public void loadUserByUsername_ExistingEmail_ExpectUserDetailsReturned() {
        var user = new User();
        user.setEmail("user@gmail.com");
        user.setDeleted(false);


        when(userRepository.findByEmail(user.getEmail().toLowerCase())).thenReturn(Optional.of(user));

        var userDetails = userService.loadUserByUsername(user.getEmail());

        assertThat(userDetails)
                .hasFieldOrPropertyWithValue("email", "user@gmail.com")
                .hasFieldOrPropertyWithValue("deleted", false);
    }

}
