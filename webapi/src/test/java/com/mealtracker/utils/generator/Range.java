package com.mealtracker.utils.generator;

public class Range {
    private final int min;
    private final int max;
    private final int increaseBy;

    public Range(int min, int max, int increaseBy) {
        this.min = min;
        this.max = max;
        this.increaseBy = increaseBy;
    }

    public int randomValue() {
        int refinedMin = min / increaseBy;
        int refinedMax = max / increaseBy;

        int randomBase = RandomGenerator.randomInRange(refinedMin, refinedMax);
        return randomBase * increaseBy;
    }
}
