package com.mealtracker.utils.matchers;

import com.mealtracker.TestUser;
import com.mealtracker.security.CurrentUser;
import org.mockito.ArgumentMatcher;

import static org.mockito.ArgumentMatchers.argThat;

public class CurrentUserMatchers {

    public static CurrentUser eq(TestUser testUser) {
        return argThat(new IdMatcher(testUser));
    }

    static class IdMatcher implements ArgumentMatcher<CurrentUser> {

        private final TestUser expectedUser;

        IdMatcher(TestUser expectedUser) {
            this.expectedUser = expectedUser;
        }

        @Override
        public boolean matches(CurrentUser actual) {
            return expectedUser.getId() == actual.getId();
        }
    }
}
