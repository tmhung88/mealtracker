package com.mealtracker.domains;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {
    private static final int DISABLED_CALORIE_LIMIT = 0;

    @Test
    public void isDailyCalorieLimitExceeded_DailyLimitDisabled_ExpectNoAlert() {
        assertThat(user(DISABLED_CALORIE_LIMIT).isDailyCalorieLimitExceeded(10000)).isFalse();
    }

    @Test
    public void isDailyCalorieLimitExceeded_ConsumptionLessThanLimit_ExpectNoAlert() {
        assertThat(user(5000).isDailyCalorieLimitExceeded(4999)).isFalse();
    }

    @Test
    public void isDailyCalorieLimitExceeded_ConsumptionExceedLimit_ExpectAlert() {
        assertThat(user(5000).isDailyCalorieLimitExceeded(5000)).isTrue();
    }


    @Test
    public void isEnabled_UserDeleted_ExpectDisabled() {
        var user = new User();
        user.setDeleted(true);
        assertThat(user.isEnabled()).isFalse();
    }

    @Test
    public void isEnabled_UserNotDeleted_ExpectEnabled() {
        var user = new User();
        user.setDeleted(false);
        assertThat(user.isEnabled()).isTrue();
    }


    User user(int calorieLimit) {
        var settings = new UserSettings();
        settings.setDailyCalorieLimit(calorieLimit);
        var user = new User();
        user.setUserSettings(settings);
        return user;
    }
}
