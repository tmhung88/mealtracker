package com.mealtracker.services.meal;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

public class ListMyMealsInputTest {

    @Test
    public void get_CriteriaMissing_ExpectNullReturned() {
        Assertions.assertThat(criteriaBuilder().build())
                .hasFieldOrPropertyWithValue("fromDate", null)
                .hasFieldOrPropertyWithValue("toDate", null)
                .hasFieldOrPropertyWithValue("fromTime", null)
                .hasFieldOrPropertyWithValue("toTime", null);
    }

    @Test
    public void get_CriteriaAvailable_ExpectCriteriaReturned() {
        var input = criteriaBuilder().fromDate("2019-01-05").toDate("2019-01-06")
                .fromTime("20:00").toTime("22:00").build();

        Assertions.assertThat(input)
                .hasFieldOrPropertyWithValue("fromDate", LocalDate.parse("2019-01-05"))
                .hasFieldOrPropertyWithValue("toDate", LocalDate.parse("2019-01-06"))
                .hasFieldOrPropertyWithValue("fromTime", LocalTime.parse("20:00"))
                .hasFieldOrPropertyWithValue("toTime", LocalTime.parse("22:00"));
    }

    ListMyMealsInputBuilder criteriaBuilder() {
        return new ListMyMealsInputBuilder();
    }
}
