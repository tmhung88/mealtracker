package com.mealtracker.repositories;

import com.mealtracker.domains.Meal;
import com.mealtracker.domains.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MealRepository extends PagingAndSortingRepository<Meal, Long> {

    Optional<Meal> findMealByIdAndDeleted(long mealId, boolean deleted);

    @Modifying
    @Query("UPDATE Meal meal SET meal.deleted = true where meal.id IN :mealIds and meal.consumer.id = :consumerId")
    void deleteConsumerMeals(@Param("mealIds") List<Long> mealIds, @Param("consumerId") Long consumerId);

    List<Meal> findMealByConsumedDateAndConsumerAndDeleted(LocalDate date, User consumer, boolean deleted);

    Slice<Meal> findByDeleted(boolean deleted, Pageable pageable);
}
