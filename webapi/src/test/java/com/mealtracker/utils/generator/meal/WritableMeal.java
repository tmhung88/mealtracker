package com.mealtracker.utils.generator.meal;

import com.mealtracker.domains.Meal;
import com.mealtracker.domains.User;
import com.mealtracker.utils.StringUtils;
import com.mealtracker.utils.generator.Writable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class WritableMeal implements Writable {

    private static final String MYSQL_INSERT_TEMPLATE = "INSERT INTO meals (id, name, calories, consumed_date, consumed_time, consumer_id, deleted) " +
            "VALUES (%s, '%s', %s, '%s', '%s', %s, %s);";

    private final Meal meal = new Meal();

    public WritableMeal id(long id) {
        meal.setId(id);
        return this;
    }

    public WritableMeal name(String name) {
        meal.setName(name);
        return this;
    }

    public WritableMeal consumedDate(LocalDate date) {
        meal.setConsumedDate(date);
        return this;
    }

    public WritableMeal consumedTime(LocalTime time) {
        meal.setConsumedTime(time);
        return this;
    }

    public WritableMeal calories(int calories) {
        meal.setCalories(calories);
        return this;
    }

    public WritableMeal consumer(long consumerId) {
        var consumer = new User();
        consumer.setId(consumerId);

        meal.setConsumer(consumer);
        return this;
    }

    public WritableMeal deleted(boolean deleted) {
        meal.setDeleted(deleted);
        return this;
    }

    public long getConsumerId() {
        return meal.getConsumer().getId();
    }

    public boolean isDeleted() {
        return meal.isDeleted();
    }

    @Override
    public String toMySQLInsert() {
        String deleted = meal.isDeleted() ? "1" : "0";
        String consumedDate = meal.getConsumedDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String consumedTime = meal.getConsumedTime().format(DateTimeFormatter.ISO_LOCAL_TIME);
        long consumerId = meal.getConsumer().getId();
        String escapedName = StringUtils.sqlEscape(meal.getName());
        return String.format(MYSQL_INSERT_TEMPLATE,meal.getId(), escapedName, meal.getCalories(), consumedDate, consumedTime, consumerId, deleted);
    }

}
