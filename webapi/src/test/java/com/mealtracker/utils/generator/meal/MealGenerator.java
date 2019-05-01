package com.mealtracker.utils.generator.meal;

import com.mealtracker.utils.ClassPathFileReader;
import com.mealtracker.utils.ClassPathFileWriter;
import com.mealtracker.utils.generator.RandomGenerator;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Generate meal data
 */
@Slf4j
public class MealGenerator {

    public static void main(String[] args) {
        var regularUserConfig = new ConsumerConfig(3, Diet.FOUR_TIMES, LocalDate.of(2019, 3, 1), LocalDate.of(2019, 5, 1), 70);
        var hungConfig = new ConsumerConfig(4, Diet.FIVE_TIMES, LocalDate.of(2019, 2, 1), LocalDate.of(2019, 5, 1), 100);
        /**
         * The configuration is to generate meals for users [100, 201).
         * - For the specific user#3, he has 4 meals/day and had meals from[2019-03-01, 2019-05-01) and 70 meals out of all his meals deleted
         * - For the specific hung#4, he has 5 meals/day and had meals from[2019-02-01, 2019-05-01) and 100 meals out of all his meals deleted
         *
         */
        var generatorConfig = MealGeneratorConfig.builder()
                .massConfigs(100, 201)
                .specificConfig(regularUserConfig)
                .specificConfig(hungConfig)
                .build();
        mealGenerator(generatorConfig).generate();
    }

    private final ClassPathFileReader fileReader = new ClassPathFileReader();
    private final ClassPathFileWriter fileWriter = new ClassPathFileWriter();
    private List<WritableMeal> meals = new LinkedList<>();
    private final ArrayList<String> dishNames = new ArrayList<>();
    private final MealGeneratorConfig config;

    public static MealGenerator mealGenerator(MealGeneratorConfig config) {
        return new MealGenerator(config);
    }

    private MealGenerator(MealGeneratorConfig config) {
        this.config = config;
    }


    public void generate() {
        var consumerConfigs = config.getConsumerConfigs();

        int increaseId = config.getStartingId();
        for (var consumerConfig : consumerConfigs) {
            var consumerMeals = generateMeals(increaseId, consumerConfig);
            meals.addAll(consumerMeals);
            increaseId += consumerMeals.size() + 1;
        }

        fileWriter.write(config.getOutputFile(), meals, WritableMeal::toMySQLInsert);
        logStatistics();

    }

    public void logStatistics() {
        long totalMealCount = meals.size();
        long totalDeletedMealCount = meals.stream().filter(WritableMeal::isDeleted).count();
        long totalUserCount = meals.stream().map(WritableMeal::getConsumerId).distinct().count();
        log.debug("{} generated and distributed across {} users in total", totalMealCount, totalUserCount);
        log.debug("{} meals deleted", totalDeletedMealCount);
        log.info("{} meals generated in total. Please find the generated file in the project directory at {}", totalDeletedMealCount, config.getOutputFile());
    }

    List<WritableMeal> generateMeals(final int startingId, ConsumerConfig consumerConfig) {
        var consumerMeals = new LinkedList<WritableMeal>();
        int increaseId = startingId;
        for (var date : consumerConfig.getAllDates()) {
            for (var eatingTime : consumerConfig.getEatingTimes()) {
                var meal = createNewMeal(increaseId, consumerConfig.getConsumerId());
                meal.consumedDate(date);
                meal.consumedTime(eatingTime);
                consumerMeals.add(meal);
                increaseId++;
            }
        }
        int currentDeletedCount = 0;
        var remainingMeals = new ArrayList<>(consumerMeals);
        while (currentDeletedCount <= consumerConfig.getNumberOfDeletedMeals() && !remainingMeals.isEmpty()) {
            int randomInd = 0;
            if (remainingMeals.size() > 1) {
                randomInd = RandomGenerator.randomInRange(0, remainingMeals.size() - 1);
            }
            var meal = remainingMeals.remove(randomInd);
            meal.deleted(true);
            currentDeletedCount++;
        }
        return consumerMeals;
    }

    WritableMeal createNewMeal(long mealId, long consumerId) {
        return new WritableMeal()
                .id(mealId)
                .consumer(consumerId)
                .calories(config.getCalories().randomValue())
                .deleted(false)
                .name(getRandomDish());
    }

    String getRandomDish() {
        int ind = RandomGenerator.randomInRange(0, readDishes().size() - 1);
        return dishNames.get(ind);
    }

    ArrayList<String> readDishes() {
        if (!dishNames.isEmpty()) {
            return dishNames;
        }
        var lines = fileReader.readFile(config.getDishNameFile());
        dishNames.addAll(lines);
        return dishNames;
    }
}
