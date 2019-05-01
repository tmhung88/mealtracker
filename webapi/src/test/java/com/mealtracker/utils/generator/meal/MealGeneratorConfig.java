package com.mealtracker.utils.generator.meal;

import com.mealtracker.utils.generator.Range;
import lombok.Data;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Data
class MealGeneratorConfig {
    private List<ConsumerConfig> consumerConfigs;

    private int startingId = 100;
    private int numberOfMeals = 20;
    private Range calories = new Range(100, 600, 20);

    private String dishNameFile = "generator/input/dish_names.txt";
    private String outputFile = "./target/generated_meals.sql";

    private MealGeneratorConfig() {

    }

    static MealGeneratorConfigBuilder builder() {
        return new MealGeneratorConfigBuilder();
    }


    static class MealGeneratorConfigBuilder {
        final List<ConsumerConfig> specificConsumerConfigs = new LinkedList<>();
        final List<ConsumerConfig> massConsumerConfigs = new LinkedList<>();

        LocalDate fromDate = LocalDate.of(2019, 4, 1);
        LocalDate toDate = LocalDate.of(2019, 5, 1);
        Diet diet = Diet.THREE_TIMES;
        Range numberOfDeletedMeals = new Range(0, 4, 1);

        MealGeneratorConfig build() {
            var allConsumerConfigs = new LinkedList<ConsumerConfig>();
            allConsumerConfigs.addAll(massConsumerConfigs);
            allConsumerConfigs.addAll(specificConsumerConfigs);

            var generatorConfig = new MealGeneratorConfig();
            generatorConfig.setConsumerConfigs(allConsumerConfigs);
            return generatorConfig;
        }

        MealGeneratorConfigBuilder specificConfig(ConsumerConfig consumerConfig) {
            specificConsumerConfigs.add(consumerConfig);
            return this;
        }

        MealGeneratorConfigBuilder massConfigs(long consumerIdFrom, long consumerIdTo) {
            massConsumerConfigs.clear();
            for (long consumerId = consumerIdFrom; consumerId < consumerIdTo; consumerId++) {
                var consumerConfig = new ConsumerConfig(consumerId, diet, fromDate, toDate, numberOfDeletedMeals.randomValue());
                massConsumerConfigs.add(consumerConfig);
            }
            return this;
        }

    }
}
