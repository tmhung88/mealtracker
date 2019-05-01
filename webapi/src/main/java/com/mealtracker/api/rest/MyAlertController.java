package com.mealtracker.api.rest;

import com.mealtracker.payloads.CalorieAlertRequest;
import com.mealtracker.payloads.CalorieAlertResponse;
import com.mealtracker.payloads.SuccessEnvelop;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/users/me/alerts")
public class MyAlertController {

    @GetMapping("/calorie")
    public SuccessEnvelop<CalorieAlertResponse> getCalorieAlert(@Valid CalorieAlertRequest request) {
        return CalorieAlertResponse.of();
    }
}
