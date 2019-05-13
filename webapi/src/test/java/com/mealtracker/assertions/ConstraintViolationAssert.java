package com.mealtracker.assertions;

import org.assertj.core.api.AbstractAssert;

import javax.validation.ConstraintViolation;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mealtracker.assertions.ConstraintViolationAssert.Message.EMAIL_FORMAT;
import static com.mealtracker.assertions.ConstraintViolationAssert.Message.LOCAL_DATE_FORMAT;
import static com.mealtracker.assertions.ConstraintViolationAssert.Message.LOCAL_TIME_FORMAT;
import static com.mealtracker.assertions.ConstraintViolationAssert.Message.MAX_FORMAT;
import static com.mealtracker.assertions.ConstraintViolationAssert.Message.MIN_FORMAT;
import static com.mealtracker.assertions.ConstraintViolationAssert.Message.NOT_NULL;
import static com.mealtracker.assertions.ConstraintViolationAssert.Message.POSITIVE_OR_ZERO_FORMAT;
import static com.mealtracker.assertions.ConstraintViolationAssert.Message.SIZE_FORMAT;
import static com.mealtracker.assertions.ConstraintViolationAssert.Message.VALUE_IN_LIST_FORMAT;

public class ConstraintViolationAssert<T> extends AbstractAssert<ConstraintViolationAssert<T>, Set<ConstraintViolation<T>>> {


    private Set<ConstraintViolation<T>> violations;

    public ConstraintViolationAssert(Set<ConstraintViolation<T>> constraintViolations) {
        super(constraintViolations, ConstraintViolationAssert.class);
        violations = constraintViolations;
    }

    public static <T> ConstraintViolationAssert<T> assertThat(Set<ConstraintViolation<T>> actual) {
        return new ConstraintViolationAssert(actual);
    }

    public ConstraintViolationAssert<T> violate(String fieldName, String message) {
        var allViolations = getAllViolations(fieldName);
        if (allViolations.isEmpty()) {
            failWithMessage("Expected validation result has the error field <%s> but %s", fieldName, getFieldSummary());
        }

        var violationOptional = allViolations.stream().filter(violation -> violation.getMessage().equals(message)).findFirst();
        if (violationOptional.isEmpty()) {
            failWithMessage("Expected the field <%s> with the error <%s> but %s", fieldName, message, getErrorSummary(fieldName));
        }
        return this;
    }

    public ConstraintViolationAssert<T> violateSize(String fieldName, int min, int max) {
        violate(fieldName, String.format(SIZE_FORMAT, min, max));
        return this;
    }

    public ConstraintViolationAssert<T> violateValueInList(String fieldName, String... values) {
        violate(fieldName, String.format(VALUE_IN_LIST_FORMAT, Arrays.toString(values)));
        return this;
    }

    public ConstraintViolationAssert<T> violateMin(String fieldName, int min) {
        violate(fieldName, String.format(MIN_FORMAT, min));
        return this;
    }

    public ConstraintViolationAssert<T> violateMax(String fieldName, int max) {
        violate(fieldName, String.format(MAX_FORMAT, max));
        return this;
    }

    public ConstraintViolationAssert<T> violatePositiveOrZero(String fieldName) {
        violate(fieldName, POSITIVE_OR_ZERO_FORMAT);
        return this;
    }
    public ConstraintViolationAssert<T> violateEmailFormat(String fieldName) {
        violate(fieldName, EMAIL_FORMAT);
        return this;
    }

    public ConstraintViolationAssert<T> violateNotNull(String fieldName) {
        violate(fieldName, NOT_NULL);
        return this;
    }

    public ConstraintViolationAssert<T> violateLocalDateFormat(String fieldName) {
        violate(fieldName, LOCAL_DATE_FORMAT);
        return this;
    }

    public ConstraintViolationAssert<T> violateLocalTimeFormat(String fieldName) {
        violate(fieldName, LOCAL_TIME_FORMAT);
        return this;
    }

    private List<ConstraintViolation> getAllViolations(String fieldName) {
        return this.violations.stream().filter(violation -> violation.getPropertyPath().toString().equals(fieldName)).collect(Collectors.toList());
    }

    private String getErrorSummary(String fieldName) {
        var violations = getAllViolations(fieldName);
        if (violations.isEmpty()) {
            return "there was no error";
        }

        var errorSummary = new StringBuilder();
        errorSummary.append(String.format("found <%s errors>: \n", violations.size()));
        for (var violation : violations) {
            errorSummary.append(" - ");
            errorSummary.append(violation.getMessage());
        }
        return errorSummary.toString();
    }

    private String getFieldSummary() {
        var allFields = this.violations.stream().map(violation -> violation.getPropertyPath().toString()).distinct().collect(Collectors.toList());
        if (allFields.isEmpty()) {
            return "there was no error";
        }

        return String.format("found <%s fields> %s that had errors", allFields.size(), Arrays.toString(allFields.toArray()));
    }


    static class Message {
        static final String NOT_NULL = "must not be null";
        static final String LOCAL_DATE_FORMAT = "Date should be in this format yyyy-MM-dd";
        static final String LOCAL_TIME_FORMAT = "Time should be in this format hh:mm";
        static final String EMAIL_FORMAT = "must be a well-formed email address";
        static final String MIN_FORMAT = "must be greater than or equal to %s";
        static final String MAX_FORMAT = "must be less than or equal to %s";
        static final String VALUE_IN_LIST_FORMAT = "The given value is not valid. Possible values are %s";
        static final String POSITIVE_OR_ZERO_FORMAT = "must be greater than or equal to 0";
        static final String SIZE_FORMAT = "size must be between %s and %s";
    }
}
