package com.mealtracker.services;

import com.mealtracker.repositories.MealRepository;
import com.mealtracker.security.CurrentUser;
import com.mealtracker.services.mymeal.MyMealInput;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class MyMealServiceTest {

    @InjectMocks
    private MyMealService myMealService;

    @Mock
    private MealRepository mealRepository;

    @Test
    public void addMeal_AnyMeal_ExpectStoredSuccessfully() {

    }
}
