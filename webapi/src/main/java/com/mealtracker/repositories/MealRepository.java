package com.mealtracker.repositories;

import com.mealtracker.domains.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MealRepository extends JpaRepository<Meal, Long> {
    // TODO: Implement it

    Optional<Meal> findMealByIdAndDeleted(long mealId, boolean deleted);

    // TODO: implement it
    @Query("DELETE FROM Meal meal WHERE meal.id IN :mealIds")
    void deleteWithIds(@Param("mealIds") List<Long> mealIds);
}
