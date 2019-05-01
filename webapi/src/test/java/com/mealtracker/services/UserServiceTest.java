package com.mealtracker.services;

import com.mealtracker.AppErrorCode;
import com.mealtracker.assertions.AppAssertions;
import com.mealtracker.domains.User;
import com.mealtracker.domains.UserSettings;
import com.mealtracker.exceptions.BadRequestAppException;
import com.mealtracker.repositories.UserRepository;
import com.mealtracker.services.user.UserRegistrationInput;
import com.mealtracker.services.user.UserService;
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
        var registrationInput = validRegistrationInput();
        registrationInput.setEmail("existing@mail.com");

        when(userRepository.findByEmail(registrationInput.getEmail())).thenReturn(Optional.of(new User()));

        AppAssertions.assertThatThrownBy(() -> userService.registerUser(registrationInput))
                .isInstanceOf(BadRequestAppException.class)
                .hasError(AppErrorCode.SPECIFIC_BAD_INPUT, "Email existing@mail.com is already taken");

    }

    @Test
    public void registerUser_ValidUser_ExpectUserStoredWithEncryptedPassword() {
        String encryptedPassword = "dfasdl57280573nl;adjfdslfj";
        var registrationInput = validRegistrationInput();
        when(userRepository.findByEmail(registrationInput.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registrationInput.getPassword())).thenReturn(encryptedPassword);

        User expectation = registrationInput.toUser();
        expectation.setEncryptedPassword(encryptedPassword);
        expectation.setDeleted(true);
        expectation.setRole(REGULAR_USER);
        expectation.setUserSettings(new UserSettings());

        userService.registerUser(registrationInput);

        verify(userRepository).save(registeredUser(expectation));
    }

    private UserRegistrationInput validRegistrationInput() {
        var registrationInput = new UserRegistrationInput();
        registrationInput.setEmail("superman@gmail.com");
        registrationInput.setPassword("helloworld");
        registrationInput.setFullName("Superman");
        return registrationInput;
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
