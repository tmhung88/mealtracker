package com.mealtracker.utils.generator.meal;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.mealtracker.utils.generator.meal.EatingTime.BREAKFAST;
import static com.mealtracker.utils.generator.meal.EatingTime.DINNER;
import static com.mealtracker.utils.generator.meal.EatingTime.EARLY_MORNING;
import static com.mealtracker.utils.generator.meal.EatingTime.LUNCH;
import static com.mealtracker.utils.generator.meal.EatingTime.MID_NIGHT;

public enum Diet {
    THREE_TIMES(BREAKFAST, LUNCH, DINNER),
    FOUR_TIMES(BREAKFAST, LUNCH, DINNER, MID_NIGHT),
    FIVE_TIMES(EARLY_MORNING, BREAKFAST, LUNCH, DINNER, MID_NIGHT),
    ;

    private List<EatingTime> eatingTimes;

    Diet(EatingTime... eatingTimes) {
        this.eatingTimes = Arrays.asList(eatingTimes);
    }

    public List<LocalTime> getEatingTimes() {
        return eatingTimes.stream().map(EatingTime::getTimeSlot).collect(Collectors.toList());
    }
}
