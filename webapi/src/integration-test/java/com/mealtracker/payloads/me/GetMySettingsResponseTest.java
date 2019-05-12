package com.mealtracker.payloads.me;

import com.mealtracker.domains.UserSettings;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GetMySettingsResponseTest {

    @Test
    public void envelop_SettingsNull_ExpectNoCalorieReturn() {
        assertThat(GetMySettingsResponse.envelop(null).getData().getDailyCalorieLimit()).isNull();
    }

    @Test
    public void envelop_SettingsAvailable_ExpectCalorieReturn() {
        var setting = new UserSettings();
        setting.setDailyCalorieLimit(567);
        assertThat(GetMySettingsResponse.envelop(setting).getData().getDailyCalorieLimit()).isEqualTo(567);
    }
}
