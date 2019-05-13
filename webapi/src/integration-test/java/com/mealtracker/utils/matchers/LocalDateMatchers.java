package com.mealtracker.utils.matchers;

import org.mockito.ArgumentMatcher;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.argThat;

public class LocalDateMatchers {
    public static LocalDate eq(String date) {
        return argThat(new StringMatcher(date));
    }
    public static LocalDate eq(LocalDate localDate) {
        return argThat(new StringMatcher(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE)));
    }

    static class StringMatcher implements ArgumentMatcher<LocalDate> {

        private final String expectation;

        StringMatcher(String expectation) {
            this.expectation = expectation;
        }

        @Override
        public boolean matches(LocalDate actual) {
            return LocalDate.parse(expectation).isEqual(actual);
        }
    }
}
