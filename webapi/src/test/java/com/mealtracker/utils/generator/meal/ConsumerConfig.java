package com.mealtracker.utils.generator.meal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;


class ConsumerConfig {
    private final long consumerId;
    private final Diet diet;
    private final LocalDate fromDate;
    private final LocalDate toDate;
    private final int numberOfDeletedMeals;

    public ConsumerConfig(long consumerId, Diet diet, LocalDate fromDate, LocalDate toDate, int numberOfDeletedMeals) {
        this.consumerId = consumerId;
        this.diet = diet;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.numberOfDeletedMeals = numberOfDeletedMeals;
    }

    List<LocalDate> getAllDates() {
        if (fromDate.isAfter(toDate)) {
            var message = String.format("For consumer config %s, FromDate should be before ToDate", consumerId);
            throw new IllegalArgumentException(message);
        }
        var dates = new LinkedList<LocalDate>();
        var date = fromDate;
        while (date.isBefore(toDate)) {
            dates.addLast(date);
            date = date.plusDays(1);
        }
        return dates;
    }
    
    List<LocalTime> getEatingTimes() {
        return diet.getEatingTimes();
    }

    long getConsumerId() {
        return consumerId;
    }

    int getNumberOfDeletedMeals() {
        return numberOfDeletedMeals;
    }
}
