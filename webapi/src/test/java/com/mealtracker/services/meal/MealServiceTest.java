package com.mealtracker.services.meal;

import com.mealtracker.assertions.AppAssertions;
import com.mealtracker.domains.Meal;
import com.mealtracker.domains.User;
import com.mealtracker.payloads.Error;
import com.mealtracker.repositories.MealRepository;
import com.mealtracker.services.meal.MyMealService;
import com.mealtracker.services.pagination.PageableBuilder;
import com.mealtracker.services.user.ListUsersInput;
import com.mealtracker.services.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MealServiceTest {
    private static final Error MEAL_NOT_FOUND = Error.of(40401, "The given meal does not exist");

    @InjectMocks
    private MealService mealService;

    @Mock
    private MealRepository mealRepository;

    @Mock
    private PageableBuilder pageableBuilder;

    @Mock
    private UserService userService;

    @Test
    public void addMeal_ValidInput_ExpectMealInfoCompletelyStored() {
        var consumer = consumer(15L);
        var input = mealInput(consumer);
        input.setName("Ice cream");
        input.setCalories(100);
        input.setConsumedDate("2015-05-04");
        input.setConsumedTime("00:00");
        when(userService.getExistingUser(consumer.getId())).thenReturn(consumer);

        mealService.addMeal(input);

        verify(mealRepository).save(MealMatchers.eq(MealMatchers.fields()
                .name(input.getName())
                .calories(input.getCalories())
                .consumerId(input.getConsumerId())
                .consumedDate(input.getConsumedDate())
                .consumedTime(input.getConsumedTime())
                .deleted(false)
        ));

    }

    @Test
    public void updateMeal_MealIdNotFound_ExpectException() {
        var unknownMealId = 222L;
        when(mealRepository.findExistingMeal(unknownMealId, null)).thenReturn(Optional.empty());
        AppAssertions.assertThatThrownBy(() -> mealService.updateMeal(unknownMealId, mock(MealInput.class)))
                .hasError(MEAL_NOT_FOUND);

    }

    @Test
    public void updateMeal_ValidInput_ExpectNewMealInfoUpdated() {
        var differentConsumer = consumer(20L);
        var input = mealInput(differentConsumer);
        input.setName("Different name");
        input.setConsumedDate("2000-01-01");
        input.setConsumedTime("00:04");
        input.setCalories(1000);

        var existingMeal = existingMeal(3L);
        when(userService.getExistingUser(input.getConsumerId())).thenReturn(differentConsumer);
        when(mealRepository.findExistingMeal(existingMeal.getId(), null)).thenReturn(Optional.of(existingMeal));

        mealService.updateMeal(existingMeal.getId(), input);

        verify(mealRepository).save(MealMatchers.eq(MealMatchers.fields()
                .id(existingMeal.getId())
                .name(input.getName())
                .calories(input.getCalories())
                .consumerId(input.getConsumerId())
                .consumedDate(input.getConsumedDate())
                .consumedTime(input.getConsumedTime())
                .deleted(false)
        ));
    }

    @Test
    public void deleteMeals_ExpectPerformSoftDeleteOnGivenMealIds() {
        var mealIds = Arrays.asList(4L, 9L);
        var input = new DeleteMealsInput();
        input.setIds(mealIds);

        mealService.deleteMeals(input);

        verify(mealRepository).softDelete(mealIds, null);
    }

    @Test
    public void listMeals_ExpectExistingMealsListed() {
        var input = mock(ListMealsInput.class);
        var pageable = mock(Pageable.class);
        when(pageableBuilder.build(input)).thenReturn(pageable);

        mealService.listMeals(input);

        verify(mealRepository).listExistingMeals(pageable);
    }

    @Test
    public void getMeal_MealNotFound_ExpectException() {
        var unknownMealId = 19L;
        when(mealRepository.findExistingMeal(unknownMealId, null)).thenReturn(Optional.empty());
        AppAssertions.assertThatThrownBy(() -> mealService.getMeal(unknownMealId))
                .hasError(MEAL_NOT_FOUND);
    }

    @Test
    public void getMeal_MealFound_ExpectDetailsReturned() {
        var existingMeal = existingMeal(199L);
        when(mealRepository.findExistingMeal(existingMeal.getId(), null)).thenReturn(Optional.of(existingMeal));

        Assertions.assertThat(mealService.getMeal(existingMeal.getId())).isEqualTo(existingMeal);
    }


    MealInput mealInput(User consumer) {
        var input = new MealInput();
        input.setConsumerId(consumer.getId());
        return input;
    }


    User consumer(Long id) {
        var user = new User();
        user.setId(id);
        return user;
    }

    Meal existingMeal(Long mealId) {
        var consumer = new User();
        consumer.setId(4551L);

        var meal = new Meal();
        meal.setId(mealId);
        meal.setConsumer(consumer);
        meal.setName("Old name");
        meal.setCalories(472);
        meal.setDeleted(false);
        return meal;
    }
}
