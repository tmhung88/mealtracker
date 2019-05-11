package com.mealtracker.repositories;

import com.mealtracker.domains.Meal;
import com.mealtracker.domains.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface MealRepository extends PagingAndSortingRepository<Meal, Long> {


    @Modifying
    @Query("UPDATE Meal meal SET meal.deleted = false WHERE meal.id IN :mealIds " +
            "AND (:consumerId IS NULL OR :consumerId = meal.consumer.id)")
    void softDelete(@Param("mealIds") List<Long> mealIds, @Param("consumerId") Long consumerId);

    List<Meal> findMealByConsumedDateAndConsumerAndDeleted(LocalDate date, User consumer, boolean deleted);

    @Query("SELECT meal FROM Meal meal WHERE meal.consumer.id = :consumerId AND deleted = false " +
            "AND (:fromDate IS NULL OR :fromDate <= meal.consumedDate) " +
            "AND (:toDate IS NULL OR :toDate > meal.consumedDate) " +
            "AND (:fromTime IS NULL OR :fromTime <= meal.consumedTime) " +
            "AND (:toTime IS NULL OR :toTime > meal.consumedTime)")
    Page<Meal> filterMyMeals(@Param("consumerId") long consumerId,
                             @Param("fromDate") LocalDate fromDate,
                             @Param("toDate") LocalDate toDate,
                             @Param("fromTime") LocalTime fromTime,
                             @Param("toTime") LocalTime toTime,
                             Pageable pageable);


    @EntityGraph(value = "Meal.consumer", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT meal FROM Meal meal JOIN meal.consumer consumer " +
            "WHERE meal.deleted = false AND consumer.deleted = false AND meal.id = :mealId " +
            "AND (:consumerId IS NULL OR :consumerId = consumer.id)")
    Optional<Meal> findExistingMeal(@Param("mealId") long mealId, @Param("consumerId") Long consumerId);


    @EntityGraph(value = "Meal.consumer", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT meal FROM Meal meal JOIN meal.consumer consumer WHERE meal.deleted = false AND consumer.deleted = false")
    Page<Meal> listExistingMeals(Pageable pageable);
}
