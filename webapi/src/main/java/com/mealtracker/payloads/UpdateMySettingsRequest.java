package com.mealtracker.payloads;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class UpdateMySettingsRequest {

    @Min(0)
    @Max(50000)
    private Integer dailyCalorieLimit;
}
