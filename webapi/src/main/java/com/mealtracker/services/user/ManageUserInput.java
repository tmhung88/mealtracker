package com.mealtracker.services.user;

import com.mealtracker.domains.Role;
import com.mealtracker.domains.User;
import com.mealtracker.domains.UserSettings;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ManageUserInput extends UserInput {

    @NotNull
    private Role role;

    @NotNull
    private Integer dailyCalorieLimit;

    public User toUser() {
        var settings = new UserSettings();
        settings.setDailyCalorieLimit(dailyCalorieLimit);

        var user = super.toUser();
        user.setRole(role);
        user.setUserSettings(settings);
        return user;
    }

    public void merge(User existingUser) {
        existingUser.setFullName(getFullName());
        existingUser.setPassword(getPassword());
        existingUser.setEmail(getEmail());
        existingUser.setRole(role);
        existingUser.getUserSettings().setDailyCalorieLimit(dailyCalorieLimit);
    }
}
