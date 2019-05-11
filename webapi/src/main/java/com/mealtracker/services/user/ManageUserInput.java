package com.mealtracker.services.user;

import com.mealtracker.domains.Role;
import com.mealtracker.domains.User;
import com.mealtracker.domains.UserSettings;
import com.mealtracker.validation.OnAdd;
import com.mealtracker.validation.OnUpdate;
import com.mealtracker.validation.ValueInList;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ManageUserInput extends UserInput {

    @NotNull(groups = {OnAdd.class, OnUpdate.class})
    @ValueInList(value = {"REGULAR_USER", "USER_MANAGER", "ADMIN"}, groups = {OnAdd.class, OnUpdate.class})
    private String role;

    @NotNull(groups = {OnAdd.class, OnUpdate.class})
    private Integer dailyCalorieLimit;

    public User toUser() {
        var settings = new UserSettings();
        settings.setDailyCalorieLimit(dailyCalorieLimit);

        var user = super.toUser();
        user.setRole(getRole());
        user.setUserSettings(settings);
        return user;
    }

    public void merge(User existingUser) {
        existingUser.setFullName(getFullName());
        existingUser.setPassword(getPassword());
        existingUser.setEmail(getEmail());
        existingUser.setRole(getRole());
        existingUser.getUserSettings().setDailyCalorieLimit(dailyCalorieLimit);
    }

    public Role getRole() {
        return Role.valueOf(role);
    }
}
