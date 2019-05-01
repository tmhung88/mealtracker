package com.mealtracker.api.rest;

import com.mealtracker.payloads.CalorieAlertRequest;
import com.mealtracker.payloads.CalorieAlertResponse;
import com.mealtracker.payloads.SuccessEnvelop;
import com.mealtracker.security.CurrentUser;
import com.mealtracker.services.alert.CalorieAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/users/me/alerts")
public class MyAlertController {

    @Autowired
    private CalorieAlertService calorieAlertService;

    @GetMapping("/calorie")
    public SuccessEnvelop<CalorieAlertResponse> getCalorieAlert(@Valid CalorieAlertRequest request, CurrentUser currentUser) {
        var calorieAlert = calorieAlertService.getAlert(request.getDate(), currentUser);
        return CalorieAlertResponse.of(calorieAlert);
    }
}
