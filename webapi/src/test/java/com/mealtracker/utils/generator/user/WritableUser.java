package com.mealtracker.utils.generator.user;


import com.mealtracker.domains.Role;
import com.mealtracker.domains.User;
import com.mealtracker.domains.UserSettings;
import com.mealtracker.utils.generator.Writable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WritableUser implements Writable {
    private static final String NAME_SEPARATOR = " ";
    private static final int FIRST_NAME = 0;
    private static final int LAST_NAME = 1;
    private static final String EMAIL_TEMPLATE = "%s_%s_%s@abc.com";
    private static final String MYSQL_INSERT_TEMPLATE = "INSERT INTO mealtracker.users (id, email, encrypted_password, role, deleted, full_name, daily_calorie_limit) VALUES " +
            "(%s, '%s', '%s', %s, %s, '%s', %s);";


    private final User user;

    public WritableUser() {
        var settings = new UserSettings();
        user = new User();
        user.setUserSettings(settings);
    }

    public WritableUser id(long id) {
        user.setId(id);
        return this;
    }

    public WritableUser deleted(boolean deleted) {
        user.setDeleted(deleted);
        return this;
    }

    public WritableUser dailyCalorieLimit(int calorieLimit) {
        user.getUserSettings().setDailyCalorieLimit(calorieLimit);
        return this;
    }

    public WritableUser role(Role role) {
        user.setRole(role);
        return this;
    }

    public WritableUser encryptedPassword(String encryptedPassword) {
        user.setEncryptedPassword(encryptedPassword);
        return this;
    }

    public WritableUser fullName(String fullName) {
        user.setFullName(fullName);
        if (user.getId() == 0) {
            log.warn("Please make sure set unique id for this user {} so that his email is unique", fullName);
        }
        user.setEmail(fullNameToEmail(fullName, user.getId()));
        return this;
    }


    public boolean isAdmin() {
        return user.getRole() == Role.ADMIN;
    }

    public boolean isRegularUser() {
        return user.getRole() == Role.REGULAR_USER;
    }

    public boolean isUserManager() {
        return user.getRole() == Role.USER_MANAGER;
    }

    public boolean isDeleted() {
        return user.isDeleted();
    }

    private static String fullNameToEmail(String fullName, long uniqueId) {
        String[] parts = fullName.toLowerCase().split(NAME_SEPARATOR);
        return String.format(EMAIL_TEMPLATE, parts[FIRST_NAME], parts[LAST_NAME], uniqueId);
    }

    @Override
    public String toMySQLInsert() {
        String deleted = user.isDeleted() ? "1" : "0";
        return String.format(MYSQL_INSERT_TEMPLATE, user.getId(),
                user.getEmail(),
                user.getEncryptedPassword(),
                user.getRole().ordinal(), deleted,
                user.getFullName(),
                user.getUserSettings().getDailyCalorieLimit());
    }
}
