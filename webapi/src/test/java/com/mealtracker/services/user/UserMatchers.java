package com.mealtracker.services.user;

import com.mealtracker.domains.Privilege;
import com.mealtracker.domains.User;
import org.mockito.ArgumentMatcher;

import java.util.Optional;

import static com.mealtracker.domains.Role.REGULAR_USER;
import static org.mockito.ArgumentMatchers.argThat;

public class UserMatchers {

    public static OptionalUser fields() {
        return new OptionalUser();
    }

    public static User eq(RegisterUserInput input) {
        return argThat(new RegisteredUserMatcher(input));
    }
    public static User eq(OptionalUser optionalUser) {
        return argThat(new OptionalUserMatcher(optionalUser));
    }

    static class RegisteredUserMatcher implements ArgumentMatcher<User> {

        private final RegisterUserInput expectation;

        RegisteredUserMatcher(RegisterUserInput input) {
            this.expectation = input;
        }

        @Override
        public boolean matches(User actual) {
            return actual.getEmail().equals(expectation.getEmail())
                    && actual.getUserSettings().getDailyCalorieLimit() == 0
                    && actual.getPrivileges().size() == 1 && actual.getPrivileges().contains(Privilege.MY_MEALS)
                    && actual.getFullName().equals(expectation.getFullName())
                    && actual.getPassword().equals(expectation.getPassword())
                    && actual.getRole() == REGULAR_USER;
        }
    }

    static class OptionalUserMatcher implements ArgumentMatcher<User> {

        private final OptionalUser expectation;

        OptionalUserMatcher(OptionalUser expectation) {
            this.expectation = expectation;
        }

        @Override
        public boolean matches(User actual) {
            return expectation.id.map(i -> i.equals(actual.getId())).orElse(true) &&
                    expectation.email.map(e -> e.equals(actual.getEmail())).orElse(true) &&
                    expectation.encryptedPassword.map(e -> e.equals(actual.getEncryptedPassword())).orElse(true);
        }
    }

    static class OptionalUser {
        private Optional<Long> id = Optional.empty();
        private Optional<String> email = Optional.empty();
        private Optional<String> encryptedPassword = Optional.empty();

        public OptionalUser id(Long id) {
            this.id = Optional.ofNullable(id);
            return this;
        }

        public OptionalUser email(String email) {
            this.email = Optional.ofNullable(email);
            return this;
        }

        public OptionalUser encryptedPassword(String encryptedPassword) {
            this.encryptedPassword = Optional.ofNullable(encryptedPassword);
            return this;
        }
    }
}
