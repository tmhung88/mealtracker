package com.mealtracker.utils.generator.user;

import com.mealtracker.utils.generator.Range;
import lombok.Data;

@Data
public class UserGeneratorConfig {
    private int startingId = 11;
    private int numberOfUsers = 20;
    private Range calorieLimit = new Range(0, 10000, 100);
    private int numberOfAdmins = 0;
    private int numberOfUserManagers = 0;
    private int numberOfDeletedUsers = 0;
    /**
     * Password: test1234
     */
    private String encryptedPassword = "$2a$10$xiohcq/oqfYE281xFiB6Oub3X.9idVUplOT08iKX6zwP9bYrvxX4m";
    private String fullNameFile = "generator/input/full_names.txt";
    private String outputFile = "./target/generated_users.sql";
}
