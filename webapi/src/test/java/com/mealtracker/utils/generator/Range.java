package com.mealtracker.utils.generator;

import lombok.Value;

@Value
public class Range {
    private final int min;
    private final int max;
    private final int increaseBy;

    public int randomValue() {
        int refinedMin = min / increaseBy;
        int refinedMax = max / increaseBy;

        int randomBase = RandomGenerator.randomInRange(refinedMin, refinedMax);
        return randomBase * increaseBy;
    }
}
