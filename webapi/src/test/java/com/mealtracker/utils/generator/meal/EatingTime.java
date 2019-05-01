package com.mealtracker.utils.generator.meal;

import com.mealtracker.utils.generator.RandomGenerator;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

public enum EatingTime {
    EARLY_MORNING(time(5, 0), time(5, 30), time(6, 0)),
    BREAKFAST(time(7, 0), time(7, 15), time(7, 30), time(8, 0)),
    LUNCH(time(11, 30), time(11, 45), time(12, 0), time(12, 15), time(12, 30), time(12, 45)),
    DINNER(time(19, 0), time(19, 15), time(19, 30), time(19, 45)),
    MID_NIGHT(time(23, 15), time(23, 30), time(23, 45), time(0, 15), time(0, 0), time(0, 15), time(0, 30));

    private final ArrayList<LocalTime> timeSlots;

    EatingTime(LocalTime... timeSlots) {
        this.timeSlots = new ArrayList<>(Arrays.asList(timeSlots));
    }

    public LocalTime getTimeSlot() {
        if (timeSlots.size() == 1) {
            return timeSlots.get(0);
        }
        int randomInd = RandomGenerator.randomInRange(0, timeSlots.size() - 1);
        return timeSlots.get(randomInd);
    }

    private static LocalTime time(int hour, int minute) {
        return LocalTime.of(hour, minute);
    }
}
