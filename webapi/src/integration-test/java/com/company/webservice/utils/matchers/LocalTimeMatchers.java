package com.company.webservice.utils.matchers;

import org.mockito.ArgumentMatcher;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.argThat;

public class LocalTimeMatchers {
    public static LocalTime eq(String time) {
        return argThat(new StringMatcher(time));
    }

    public static LocalTime eq(LocalTime localTime) {
        return argThat(new StringMatcher(localTime.format(DateTimeFormatter.ISO_LOCAL_TIME)));
    }

    static class StringMatcher implements ArgumentMatcher<LocalTime> {

        private final String expectation;

        StringMatcher(String expectation) {
            this.expectation = expectation;
        }

        @Override
        public boolean matches(LocalTime actual) {
            return LocalTime.parse(expectation).equals(actual);
        }
    }
}
