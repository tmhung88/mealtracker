package com.company.webservice.utils.matchers;

import com.company.webservice.security.CurrentUser;
import org.mockito.ArgumentMatcher;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.argThat;

public class CommonMatchers {

    public static OptionalCurrentUser fields() {
        return new OptionalCurrentUser();
    }

    public static CurrentUser eq(OptionalCurrentUser optionalCurrentUser) {
        return argThat(new OptionalCurrentUserMatcher(optionalCurrentUser));
    }

    public static class OptionalCurrentUserMatcher implements ArgumentMatcher<CurrentUser> {

        private final OptionalCurrentUser expectation;

        OptionalCurrentUserMatcher(OptionalCurrentUser expectation) {
            this.expectation = expectation;
        }

        @Override
        public boolean matches(CurrentUser actual) {
            return expectation.id.map(i -> i.equals(actual.getId())).orElse(true);
        }
    }

    public static class OptionalCurrentUser {
        private Optional<Long> id = Optional.empty();

        public OptionalCurrentUser id(Long id) {
            this.id = Optional.ofNullable(id);
            return this;
        }
    }
}
