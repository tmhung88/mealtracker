package com.mealtracker.api.rest;

import com.mealtracker.payloads.GetMySettingsResponse;
import com.mealtracker.payloads.SuccessEnvelop;
import com.mealtracker.security.CurrentUser;
import com.mealtracker.services.UserSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/me")
public class MeController {

    @Autowired
    private UserSettingsService userSettingsService;

    @GetMapping
    public SuccessEnvelop<GetMySettingsResponse> getMySettings(CurrentUser currentUser) {
        var userSettings = userSettingsService.getUserSettings(currentUser.getId());
        return GetMySettingsResponse.of(userSettings);
    }
}
