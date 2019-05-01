package com.mealtracker.repositories;

import com.mealtracker.domains.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MealRepository extends JpaRepository<Meal, Long> {

    Optional<Meal> findMealByIdAndDeleted(long mealId, boolean deleted);

    @Modifying
    @Query("UPDATE Meal meal SET meal.deleted = true where meal.id IN :mealIds and meal.consumer.id = :consumerId")
    void deleteConsumerMeals(@Param("mealIds") List<Long> mealIds, @Param("consumerId") Long consumerId);
}
