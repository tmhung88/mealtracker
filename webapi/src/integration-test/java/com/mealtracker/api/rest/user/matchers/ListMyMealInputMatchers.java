package com.mealtracker.api.rest.user.matchers;

import com.mealtracker.services.meal.ListMyMealsInput;
import org.mockito.ArgumentMatcher;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.argThat;

public class ListMyMealInputMatchers {

    public static OptionalListMyMealInput fields() {
        return new OptionalListMyMealInput();
    }

    public static ListMyMealsInput eq(OptionalListMyMealInput optionalInput) {
        return argThat(new OptionalListMyMealInputMatcher(optionalInput));
    }

    public static class OptionalListMyMealInputMatcher implements ArgumentMatcher<ListMyMealsInput> {

        private final OptionalListMyMealInput expectation;

        OptionalListMyMealInputMatcher(OptionalListMyMealInput expectation) {
            this.expectation = expectation;
        }

        @Override
        public boolean matches(ListMyMealsInput actual) {
            return expectation.rowsPerPage.map(r -> r == actual.getRowsPerPage()).orElse(true) &&
                    expectation.pageIndex.map(p -> p == actual.getPageIndex()).orElse(true);
        }
    }

    public static class OptionalListMyMealInput {
        private Optional<Integer> rowsPerPage;
        private Optional<Integer> pageIndex;

        public OptionalListMyMealInput rowsPerPage(Integer rowsPerPage) {
            this.rowsPerPage = Optional.ofNullable(rowsPerPage);
            return this;
        }
        public OptionalListMyMealInput pageIndex(Integer pageIndex) {
            this.pageIndex = Optional.ofNullable(pageIndex);
            return this;
        }
    }
}
