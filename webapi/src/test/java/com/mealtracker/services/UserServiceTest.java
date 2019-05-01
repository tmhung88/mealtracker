package com.mealtracker.services;

import com.mealtracker.AppErrorCode;
import com.mealtracker.assertions.AppAssertions;
import com.mealtracker.domains.User;
import com.mealtracker.domains.UserSettings;
import com.mealtracker.exceptions.BadRequestAppException;
import com.mealtracker.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.mealtracker.domains.Role.REGULAR_USER;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
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
    public void registerUser_ExistingUserEmail_ExpectException() {
        User user = validRegistrationUser();
        user.setEmail("existing@mail.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(new User()));

        AppAssertions.assertThatThrownBy(() -> userService.registerUser(user))
                .isInstanceOf(BadRequestAppException.class)
                .hasError(AppErrorCode.SPECIFIC_BAD_INPUT, "Email existing@mail.com is already taken");

    }

    @Test
    public void registerUser_ValidUser_ExpectUserStoredWithEncryptedPassword() {
        String encryptedPassword = "dfasdl57280573nl;adjfdslfj";
        User user = validRegistrationUser();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn(encryptedPassword);

        User expectation = validRegistrationUser();
        expectation.setEncryptedPassword(encryptedPassword);
        expectation.setEnabled(true);
        expectation.setRole(REGULAR_USER);
        expectation.setUserSettings(new UserSettings());

        userService.registerUser(user);

        verify(userRepository).save(registeredUser(expectation));
    }

    private User validRegistrationUser() {
        User user = new User();
        user.setEmail("superman@gmail.com");
        user.setPassword("helloworld");
        user.setFullName("Superman");
        return user;
    }

    private User registeredUser(User expectCreatedUser) {
        return argThat(new RegistrationUserMatcher(expectCreatedUser));
    }

    class RegistrationUserMatcher implements ArgumentMatcher<User> {

        private final User expectation;

        RegistrationUserMatcher(User expectation) {
            this.expectation = expectation;
        }

        @Override
        public boolean matches(User actual) {
            return expectation.getEmail().equals(actual.getEmail()) &&
                    expectation.getEncryptedPassword().equals(actual.getEncryptedPassword()) &&
                    expectation.getFullName().equals(actual.getFullName()) &&
                    expectation.getRole().equals(actual.getRole()) &&
                    expectation.getPrivileges().equals(actual.getPrivileges()) &&
                    expectation.isEnabled() == actual.isEnabled() &&
                    expectation.getUserSettings().equals(actual.getUserSettings());
        }
    }


}
