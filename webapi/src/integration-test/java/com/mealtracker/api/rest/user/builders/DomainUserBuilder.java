package com.mealtracker.api.rest.user.builders;

import com.mealtracker.domains.Role;
import com.mealtracker.domains.User;
import com.mealtracker.domains.UserSettings;

public class DomainUserBuilder {
    private Long id;
    private String email;
    private Role role;
    private String fullName;
    private int dailyCalorieLimit;

    public DomainUserBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public DomainUserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public DomainUserBuilder role(Role role) {
        this.role = role;
        return this;
    }

    public DomainUserBuilder fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public DomainUserBuilder calorieLimit(int dailyCalorieLimit) {
        this.dailyCalorieLimit = dailyCalorieLimit;
        return this;
    }

    public User build() {
        var settings = new UserSettings();
        settings.setDailyCalorieLimit(dailyCalorieLimit);
        var user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setRole(role);
        user.setFullName(fullName);
        user.setUserSettings(settings);
        return user;
    }
}
