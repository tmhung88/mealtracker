package com.company.webservice.services.user;

import com.company.webservice.domains.Privilege;
import com.company.webservice.domains.Role;
import com.company.webservice.domains.User;
import com.company.webservice.services.user.RegisterUserInput;
import org.mockito.ArgumentMatcher;

import java.util.Optional;

import static com.company.webservice.domains.Role.REGULAR_USER;
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
                    expectation.password.map(e -> e.equals(actual.getPassword())).orElse(true) &&
                    expectation.encryptedPassword.map(e -> e.equals(actual.getEncryptedPassword())).orElse(true) &&
                    expectation.fullName.map(e -> e.equals(actual.getFullName())).orElse(true) &&
                    expectation.dailyCalorieLimit.map(e -> e.equals(actual.getUserSettings().getDailyCalorieLimit())).orElse(true) &&
                    expectation.role.map(e -> e.equals(actual.getRole())).orElse(true);
        }
    }

    public static class OptionalUser {
        private Optional<Long> id = Optional.empty();
        private Optional<String> email = Optional.empty();
        private Optional<String> password = Optional.empty();
        private Optional<String> encryptedPassword = Optional.empty();
        private Optional<String> fullName = Optional.empty();
        private Optional<Integer> dailyCalorieLimit = Optional.empty();
        private Optional<Role> role = Optional.empty();

        public OptionalUser id(Long id) {
            this.id = Optional.ofNullable(id);
            return this;
        }

        public OptionalUser email(String email) {
            this.email = Optional.ofNullable(email);
            return this;
        }

        public OptionalUser password(String password) {
            this.password = Optional.ofNullable(password);
            return this;
        }

        public OptionalUser role(Role role) {
            this.role = Optional.of(role);
            return this;
        }

        public OptionalUser encryptedPassword(String encryptedPassword) {
            this.encryptedPassword = Optional.ofNullable(encryptedPassword);
            return this;
        }

        public OptionalUser fullName(String fullName) {
            this.fullName = Optional.ofNullable(fullName);
            return this;
        }

        public OptionalUser dailyCalorieLimit(Integer dailyCalorieLimit) {
            this.dailyCalorieLimit = Optional.ofNullable(dailyCalorieLimit);
            return this;
        }
    }
}
