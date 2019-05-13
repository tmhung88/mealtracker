package com.mealtracker.services.meal;

import com.mealtracker.domains.Meal;
import org.mockito.ArgumentMatcher;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.argThat;

public class MealMatchers {
    public static OptionalMeal fields() {
        return new OptionalMeal();
    }

    public static Meal eq(OptionalMeal optionalMeal) {
        return argThat(new OptionalUserMatcher(optionalMeal));
    }


    static class OptionalUserMatcher implements ArgumentMatcher<Meal> {

        private final OptionalMeal expectation;

        OptionalUserMatcher(OptionalMeal expectation) {
            this.expectation = expectation;
        }

        @Override
        public boolean matches(Meal actual) {
            return expectation.id.map(i -> i.equals(actual.getId())).orElse(true) &&
                    expectation.name.map(name -> name.equals(actual.getName())).orElse(true) &&
                    expectation.consumedDate.map(date -> date.equals(actual.getConsumedDate())).orElse(true) &&
                    expectation.consumedTime.map(time -> time.equals(actual.getConsumedTime())).orElse(true) &&
                    expectation.calories.map(calories -> calories.equals(actual.getCalories())).orElse(true) &&
                    expectation.consumerId.map(consumerId -> consumerId.equals(actual.getConsumer().getId())).orElse(true) &&
                    expectation.deleted.map(deleted -> deleted.equals(actual.isDeleted())).orElse(true);
        }
    }

    static class OptionalMeal {
        private Optional<Long> id = Optional.empty();
        private Optional<String> name = Optional.empty();
        private Optional<LocalDate> consumedDate = Optional.empty();
        private Optional<LocalTime> consumedTime = Optional.empty();
        private Optional<Integer> calories = Optional.empty();
        private Optional<Long> consumerId = Optional.empty();
        private Optional<Boolean> deleted = Optional.empty();

        public OptionalMeal id(Long id) {
            this.id = Optional.ofNullable(id);
            return this;
        }

        public OptionalMeal name(String name) {
            this.name = Optional.ofNullable(name);
            return this;
        }

        public OptionalMeal consumedDate(LocalDate consumedDate) {
            this.consumedDate = Optional.ofNullable(consumedDate);
            return this;
        }

        public OptionalMeal consumedTime(LocalTime consumedTime) {
            this.consumedTime = Optional.of(consumedTime);
            return this;
        }

        public OptionalMeal calories(Integer calories) {
            this.calories = Optional.ofNullable(calories);
            return this;
        }

        public OptionalMeal consumerId(Long consumerId) {
            this.consumerId = Optional.ofNullable(consumerId);
            return this;
        }

        public OptionalMeal deleted(Boolean deleted) {
            this.deleted = Optional.ofNullable(deleted);
            return this;
        }

    }
}
