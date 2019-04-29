package com.mealtracker.services;

import com.mealtracker.domains.Meal;
import com.mealtracker.repositories.MealRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MealServiceTest {

    @InjectMocks
    private MealService mealService;

    @Mock
    private MealRepository mealRepository;

    @Test
    public void addMeal_AnyMeal_ExpectStoredSuccessfully() {
        Meal meal = Mockito.mock(Meal.class);
        mealService.addMeal(meal);
        verify(mealRepository).save(meal);
    }
}
