package com.mealtracker.utils.matchers;

import com.mealtracker.services.user.RegisterUserInput;
import org.mockito.ArgumentMatcher;

import static org.mockito.ArgumentMatchers.argThat;

public class UserRegistrationInputMatchers {

    public static RegisterUserInput email(String expectedEmail) {
        return argThat(new EmailMatcher(expectedEmail));
    }

    static class EmailMatcher implements ArgumentMatcher<RegisterUserInput> {

        private final String expectedEmail;

        EmailMatcher(String expectedEmail) {
            this.expectedEmail = expectedEmail;
        }

        @Override
        public boolean matches(RegisterUserInput actual) {
            return expectedEmail.equals(actual.getEmail());
        }
    }
}
