package com.mealtracker.repositories;

import com.mealtracker.domains.Meal;
import com.mealtracker.domains.User;
import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class MealRepositoryIT {

    @ClassRule
    public static MySQLContainer mySQLContainer = AppDbContainer.getInstance();

    @Autowired
    private MealRepository mealRepository;

    @Test
    @Sql("classpath:repositories/meal/insert_meal_1.sql")
    @Sql(scripts = "classpath:repositories/delete_meals.sql", executionPhase = AFTER_TEST_METHOD)
    public void softDelete_MealIdsOnly_ExpectMeals_MatchId_Deleted() {
        var mealIds = Arrays.asList(1L, 3L);
        mealRepository.softDelete(mealIds, null);

        var meals = mealRepository.findAll();

        assertThat(countDeletedMeals(meals)).describedAs("Number of meals returned").isEqualTo(2);
        assertThat(mealRepository.findById(2L).get().isDeleted()).isFalse();
    }


    @Test
    @Sql("classpath:repositories/meal/insert_meal_2.sql")
    @Sql(scripts = "classpath:repositories/delete_meals.sql", executionPhase = AFTER_TEST_METHOD)
    public void softDelete_MealIds_ConsumerId_ExpectMeals_MatchConsumerIdAndMealIds_Deleted() {
        var consumerId = 1L;
        var notDeletedMealId = 3L;
        var mealIds = Arrays.asList(1L, 2L, notDeletedMealId);
        mealRepository.softDelete(mealIds, consumerId);

        var meals = mealRepository.findAll();

        assertThat(countDeletedMeals(meals)).isEqualTo(2);
        assertThat(mealRepository.findById(notDeletedMealId).get().isDeleted()).isFalse();
    }

    @Test
    @Sql("classpath:repositories/meal/insert_meal_3.sql")
    @Sql(scripts = "classpath:repositories/delete_meals.sql", executionPhase = AFTER_TEST_METHOD)
    public void findExistingMeals_GivenConsumer_GivenDate_ExpectExistingMeals() {
        var consumer = new User();
        consumer.setId(1L);
        var meals = mealRepository.findMealByConsumedDateAndConsumerAndDeleted(LocalDate.of(2018, 11, 5), consumer, false);

        assertThat(meals.size()).isEqualTo(1);
        assertThat(meals.get(0).getName()).isEqualTo("user cake");
    }

    @Test
    @Sql("classpath:repositories/meal/insert_meal_4.sql")
    @Sql(scripts = "classpath:repositories/delete_meals.sql", executionPhase = AFTER_TEST_METHOD)
    public void filterMyMeals_FromDate_ExpectMealsEqualOrAfterDateReturned() {
        var meals = mealRepository.filterMyMeals(1L, LocalDate.of(2017, 2, 10), null, null, null, PageRequest.of(0, 1000)).getContent();
        Assertions.assertThat(name(meals)).containsExactlyInAnyOrder("eat on fromDate", "eat after fromDate");
    }

    @Test
    @Sql("classpath:repositories/meal/insert_meal_5.sql")
    @Sql(scripts = "classpath:repositories/delete_meals.sql", executionPhase = AFTER_TEST_METHOD)
    public void filterMyMeals_ToDate_ExpectMealsBeforeDateReturned() {
        var meals = mealRepository.filterMyMeals(1L, null, LocalDate.of(2018, 9, 20), null, null, PageRequest.of(0, 1000)).getContent();
        Assertions.assertThat(name(meals)).containsExactlyInAnyOrder("eat before toDate");
    }

    @Test
    @Sql("classpath:repositories/meal/insert_meal_6.sql")
    @Sql(scripts = "classpath:repositories/delete_meals.sql", executionPhase = AFTER_TEST_METHOD)
    public void filterMyMeals_FromTime_ExpectMealsEqualOrAfterTimeReturned() {
        var meals = mealRepository.filterMyMeals(1L, null, null, LocalTime.of(9, 0), null, PageRequest.of(0, 1000)).getContent();
        Assertions.assertThat(name(meals)).containsExactlyInAnyOrder("eat on fromTime", "eat after fromTime");
    }

    @Test
    @Sql("classpath:repositories/meal/insert_meal_7.sql")
    @Sql(scripts = "classpath:repositories/delete_meals.sql", executionPhase = AFTER_TEST_METHOD)
    public void filterMyMeals_ToTime_ExpectMealsBeforeTimeReturned() {
        var meals = mealRepository.filterMyMeals(1L, null, null, null, LocalTime.of(15, 30), PageRequest.of(0, 1000)).getContent();
        Assertions.assertThat(name(meals)).containsExactlyInAnyOrder("eat before toTime");
    }

    @Test
    @Sql("classpath:repositories/meal/insert_meal_8.sql")
    @Sql(scripts = "classpath:repositories/delete_meals.sql", executionPhase = AFTER_TEST_METHOD)
    public void filterMyMeals_NoDateTimeCriteria_ExpectExistingMeals_ConsumerReturned() {
        var meals = mealRepository.filterMyMeals(1L, null, null, null, null, PageRequest.of(0, 1000)).getContent();
        Assertions.assertThat(name(meals)).containsExactlyInAnyOrder("eat another day", "eat another time");
    }

    long countDeletedMeals(List<Meal> meals) {
        return meals.stream().filter(Meal::isDeleted).count();
    }

    List<String> name(List<Meal> meals) {
        return meals.stream().map(Meal::getName).collect(Collectors.toList());
    }
}
