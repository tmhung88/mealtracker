package com.mealtracker.services;

import com.mealtracker.repositories.MealRepository;
import com.mealtracker.services.mymeal.MyMealService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
