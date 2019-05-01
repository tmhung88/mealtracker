package com.mealtracker.utils.generator;

import java.util.Random;

public class RandomGenerator {

    public static int randomInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
