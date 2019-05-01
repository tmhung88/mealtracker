package com.mealtracker.payloads;

import com.mealtracker.domains.UserSettings;
import lombok.Value;

@Value
public class GetMySettingsResponse {
    private final int dailyCalorieLimit;

    public static SuccessEnvelop<GetMySettingsResponse> of(UserSettings userSettings) {
        return new SuccessEnvelop<>(new GetMySettingsResponse(userSettings.getDailyCalorieLimit()));
    }
}
