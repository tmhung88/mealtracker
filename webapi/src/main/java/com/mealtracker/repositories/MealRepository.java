package com.mealtracker.repositories;

import com.mealtracker.domains.Meal;
import com.mealtracker.domains.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface MealRepository extends PagingAndSortingRepository<Meal, Long> {

    Optional<Meal> findMealByIdAndDeleted(long mealId, boolean deleted);

    @Modifying
    @Query("UPDATE Meal meal SET meal.deleted = true where meal.id IN :mealIds and meal.consumer.id = :consumerId")
    void deleteConsumerMeals(@Param("mealIds") List<Long> mealIds, @Param("consumerId") Long consumerId);

    List<Meal> findMealByConsumedDateAndConsumerAndDeleted(LocalDate date, User consumer, boolean deleted);

    @Query("SELECT meal FROM Meal meal WHERE meal.consumer.id = :consumerId AND deleted = 0 AND " +
            "meal.consumedDate >= :fromDate AND meal.consumedDate < :toDate AND " +
            "meal.consumedTime >= :fromTime AND meal.consumedTime <= :toTime")
    Page<Meal> filterUserMeals(@Param("consumerId") long consumerId,
                               @Param("fromDate") LocalDate fromDate,
                               @Param("toDate") LocalDate toDate,
                               @Param("fromTime") LocalTime fromTime,
                               @Param("toTime") LocalTime toTime,
                               Pageable pageable);
}
