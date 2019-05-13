package com.mealtracker.domains;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class UserSettingsTest {

    @Test
    public void isCalorieLimitEnabled_LimitAsZero_ExpectDisabled() {
        Assertions.assertThat(settings(0).isCalorieLimitEnabled()).isFalse();
    }

    @Test
    public void isCalorieLimitEnabled_LimitMoreThanZero_ExpectDisabled() {
        Assertions.assertThat(settings(125).isCalorieLimitEnabled()).isTrue();
    }

    UserSettings settings(int dailyCalorieLimit) {
        var settings = new UserSettings();
        settings.setDailyCalorieLimit(dailyCalorieLimit);

        return settings;
    }
}
