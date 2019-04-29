package com.mealtracker.repositories;

import com.mealtracker.domains.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long> {
}
