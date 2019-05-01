package com.mealtracker.utils.matchers;

import com.mealtracker.services.user.UserRegistrationInput;
import org.mockito.ArgumentMatcher;

import static org.mockito.ArgumentMatchers.argThat;

public class UserRegistrationInputMatchers {

    public static UserRegistrationInput email(String expectedEmail) {
        return argThat(new EmailMatcher(expectedEmail));
    }

    static class EmailMatcher implements ArgumentMatcher<UserRegistrationInput> {

        private final String expectedEmail;

        EmailMatcher(String expectedEmail) {
            this.expectedEmail = expectedEmail;
        }

        @Override
        public boolean matches(UserRegistrationInput actual) {
            return expectedEmail.equals(actual.getEmail());
        }
    }
}
