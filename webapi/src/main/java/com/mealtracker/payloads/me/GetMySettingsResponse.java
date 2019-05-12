package com.mealtracker.payloads.me;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mealtracker.domains.UserSettings;
import com.mealtracker.payloads.SuccessEnvelop;
import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetMySettingsResponse {
    private final Integer dailyCalorieLimit;

    public static SuccessEnvelop<GetMySettingsResponse> envelop(UserSettings userSettings) {
        Integer dailyCalorieLimit = userSettings == null ? null : userSettings.getDailyCalorieLimit();
        return new SuccessEnvelop<>(new GetMySettingsResponse(dailyCalorieLimit));
    }
}
