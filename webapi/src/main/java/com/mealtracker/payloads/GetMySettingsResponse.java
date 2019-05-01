package com.mealtracker.payloads;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mealtracker.domains.UserSettings;
import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetMySettingsResponse {
    private final Integer dailyCalorieLimit;

    public static SuccessEnvelop<GetMySettingsResponse> of(UserSettings userSettings) {
        Integer dailyCalorieLimit = userSettings == null ? null : userSettings.getDailyCalorieLimit();
        return new SuccessEnvelop<>(new GetMySettingsResponse(dailyCalorieLimit));
    }
}
