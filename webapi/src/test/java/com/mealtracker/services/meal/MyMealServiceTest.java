package com.mealtracker.services.meal;

import com.mealtracker.assertions.AppAssertions;
import com.mealtracker.domains.Meal;
import com.mealtracker.domains.User;
import com.mealtracker.payloads.Error;
import com.mealtracker.repositories.MealRepository;
import com.mealtracker.security.CurrentUser;
import com.mealtracker.services.pagination.PageableBuilder;
import com.mealtracker.services.user.UserMatchers;
import com.mealtracker.utils.matchers.LocalDateMatchers;
import com.mealtracker.utils.matchers.LocalTimeMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;

import static com.mealtracker.assertions.AppAssertions.assertThatThrownBy;
import static com.mealtracker.services.meal.MealMatchers.eq;
import static com.mealtracker.services.meal.MealMatchers.fields;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MyMealServiceTest {

    private static final Error MEAL_NOT_EXIST = Error.of(40401, "The given meal does not exist");
    private static final Error INVALID_DATE_RANGE = Error.of(40001, "fromDate must be before toDate");
    private static final Error INVALID_TIME_RANGE = Error.of(40001, "fromTime must be before toTime");

    @InjectMocks
    private MyMealService myMealService;

    @Mock
    private MealRepository mealRepository;

    @Mock
    private PageableBuilder pageableBuilder;

    @Test
    public void addMeal_ValidInput_ExpectMealInfoStoredCompletely() {
        var input = validInput();
        myMealService.addMeal(input, currentUser(8L));
        verify(mealRepository).save(eq(fields()
                .name(input.getName())
                .deleted(false)
                .consumedTime(input.getConsumedTime())
                .consumedDate(input.getConsumedDate())
                .calories(input.getCalories())
        ));
    }

    @Test
    public void updateMeal_MealIdNotFound_ExpectException() {
        var currentUser = currentUser(12L);
        var unknownMealId = 491L;
        when(mealRepository.findExistingMeal(unknownMealId, currentUser.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> myMealService.updateMeal(unknownMealId, validInput(), currentUser))
                .hasError(MEAL_NOT_EXIST);
    }

    @Test
    public void updateMeal_ExistingMealFound_ExpectMealInfoUpdatedCompletely() {
        var currentUser = currentUser(12L);
        var existingMeal = existingMeal(9L, currentUser.getId());
        var input = validInput();
        when(mealRepository.findExistingMeal(existingMeal.getId(), currentUser.getId())).thenReturn(Optional.of(existingMeal));

        myMealService.updateMeal(existingMeal.getId(), validInput(), currentUser);

        verify(mealRepository).save(eq(fields()
                .id(9L).name(input.getName())
                .calories(input.getCalories())
                .deleted(false).consumerId(currentUser.getId())
                .consumedDate(input.getConsumedDate()).consumedTime(input.getConsumedTime())
        ));
    }

    @Test
    public void getMeal_MealNotExisting_ExpectException() {
        var currentUser = currentUser(95L);
        var unknownMealId = 21L;
        when(mealRepository.findExistingMeal(unknownMealId, currentUser.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> myMealService.getMeal(unknownMealId, currentUser))
                .hasError(MEAL_NOT_EXIST);
    }

    @Test
    public void getMeal_MealFound_ExpectDetailsReturned() {
        var currentUser = currentUser(18L);
        var existingMeal = existingMeal(2L, currentUser.getId());
        when(mealRepository.findExistingMeal(existingMeal.getId(), currentUser.getId())).thenReturn(Optional.of(existingMeal));

        assertThat(myMealService.getMeal(existingMeal.getId(), currentUser)).isEqualTo(existingMeal);
    }

    @Test
    public void calculateDailyCalories_ExpectOnlyExitingMealConsidered() {
        var existingMeals = Arrays.asList(meal(50), meal(20), meal(30));
        var date = LocalDate.of(1990, 2, 4);
        var currentUser = currentUser(59);
        when(mealRepository.findMealByConsumedDateAndConsumerAndDeleted(
                LocalDateMatchers.eq(date), UserMatchers.eq(UserMatchers.fields().id(59L)), eq(false)))
                .thenReturn(existingMeals);

        assertThat(myMealService.calculateDailyCalories(date, currentUser)).isEqualTo(100);
    }

    @Test
    public void listMeals_FromDateAvailable_ToDateMissing_ExpectProceedFiltered() {
        var currentUser = currentUser(1L);
        var input = criteriaBuilder().fromDate("2019-05-02").build();
        var pageable = mock(Pageable.class);
        var result = mock(Page.class);
        when(pageableBuilder.build(input)).thenReturn(pageable);
        when(mealRepository.filterMyMeals(eq(currentUser.getId()),
                LocalDateMatchers.eq("2019-05-02"), isNull(), isNull(), isNull(), eq(pageable))).thenReturn(result);

        assertThat(myMealService.listMeals(input, currentUser)).isEqualTo(result);
    }

    @Test
    public void listMeals_FromDateMissing_ToDateAvailable_ExpectProceedFiltered() {
        var currentUser = currentUser(1L);
        var input = criteriaBuilder().toDate("2011-10-04").build();
        var pageable = mock(Pageable.class);
        var result = mock(Page.class);
        when(pageableBuilder.build(input)).thenReturn(pageable);
        when(mealRepository.filterMyMeals(eq(currentUser.getId()),
                isNull(), LocalDateMatchers.eq("2011-10-04"), isNull(), isNull(), eq(pageable))).thenReturn(result);

        assertThat(myMealService.listMeals(input, currentUser)).isEqualTo(result);
    }

    @Test
    public void listMeals_FromDate_NotBefore_ToDate_ExpectException() {
        var currentUser = currentUser(1L);
        var input = criteriaBuilder().fromDate("2012-11-12").toDate("2012-11-12").build();

        assertThatThrownBy(() -> myMealService.listMeals(input, currentUser)).hasError(INVALID_DATE_RANGE);
    }

    @Test
    public void listMeals_FromTimeAvailable_ToTimeMissing_ExpectProceedFiltered() {
        var currentUser = currentUser(1L);
        var input = criteriaBuilder().fromTime("20:45").build();
        var pageable = mock(Pageable.class);
        var result = mock(Page.class);
        when(pageableBuilder.build(input)).thenReturn(pageable);
        when(mealRepository.filterMyMeals(eq(currentUser.getId()),
                isNull(), isNull(), LocalTimeMatchers.eq("20:45"), isNull(), eq(pageable))).thenReturn(result);

        assertThat(myMealService.listMeals(input, currentUser)).isEqualTo(result);
    }

    @Test
    public void listMeals_FromTimeMissing_ToTimeAvailable_ExpectProceedFiltered() {
        var currentUser = currentUser(1L);
        var input = criteriaBuilder().toTime("10:05").build();
        var pageable = mock(Pageable.class);
        var result = mock(Page.class);
        when(pageableBuilder.build(input)).thenReturn(pageable);
        when(mealRepository.filterMyMeals(eq(currentUser.getId()),
                isNull(), isNull(), isNull(), LocalTimeMatchers.eq("10:05"), eq(pageable))).thenReturn(result);

        assertThat(myMealService.listMeals(input, currentUser)).isEqualTo(result);
    }

    @Test
    public void listMeals_FromTime_NotBefore_ToTime_ExpectException() {
        var currentUser = currentUser(1L);
        var input = criteriaBuilder().fromTime("12:21").toTime("12:21").build();

        assertThatThrownBy(() -> myMealService.listMeals(input, currentUser)).hasError(INVALID_TIME_RANGE);
    }

    @Test
    public void listMeals_All_CriteriaMissing_ExpectProceedFilter() {
        var currentUser = currentUser(1L);
        var input = criteriaBuilder().build();
        var pageable = mock(Pageable.class);
        var result = mock(Page.class);
        when(pageableBuilder.build(input)).thenReturn(pageable);
        when(mealRepository.filterMyMeals(eq(currentUser.getId()), isNull(), isNull(), isNull(), isNull(), eq(pageable))).thenReturn(result);

        assertThat(myMealService.listMeals(input, currentUser)).isEqualTo(result);
    }

    @Test
    public void listMeals_All_CriteriaAvailable_ExpectProceedFilter() {
        var currentUser = currentUser(1L);
        var input = criteriaBuilder().fromTime("00:00").toTime("23:59").fromDate("2000-01-01").toDate("2000-02-02").build();
        var pageable = mock(Pageable.class);
        var result = mock(Page.class);
        when(pageableBuilder.build(input)).thenReturn(pageable);
        when(mealRepository.filterMyMeals(eq(currentUser.getId()),
                LocalDateMatchers.eq("2000-01-01"),
                LocalDateMatchers.eq("2000-02-02"),
                LocalTimeMatchers.eq("00:00"),
                LocalTimeMatchers.eq("23:59"), eq(pageable))).thenReturn(result);

        assertThat(myMealService.listMeals(input, currentUser)).isEqualTo(result);
    }

    CurrentUser currentUser(long id) {
        var user = new User();
        user.setId(id);

        var currentUser = Mockito.mock(CurrentUser.class);
        when(currentUser.getId()).thenReturn(id);
        when(currentUser.toUser()).thenReturn(user);
        return currentUser;
    }

    MyMealInput validInput() {
        var input = new MyMealInput();
        input.setName("Pizza");
        input.setCalories(300);
        input.setConsumedDate("2019-02-03");
        input.setConsumedTime("19:00");
        return input;
    }

    Meal existingMeal(long mealId, long consumerId) {
        var consumer = new User();
        consumer.setId(consumerId);

        var meal = new Meal();
        meal.setId(mealId);
        meal.setCalories(921);
        meal.setName("Old name");
        meal.setConsumer(consumer);
        meal.setDeleted(false);
        meal.setConsumedTime(LocalTime.of(0, 9));
        meal.setConsumedDate(LocalDate.of(2011, 8, 1));
        return meal;
    }

    Meal meal(int calories) {
        var meal = new Meal();
        meal.setCalories(calories);
        return meal;
    }

    ListMyMealsInputBuilder criteriaBuilder() {
        return new ListMyMealsInputBuilder();
    }
}
