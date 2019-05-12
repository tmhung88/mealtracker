package com.mealtracker.api.rest;

import com.mealtracker.payloads.MessageResponse;
import com.mealtracker.payloads.SuccessEnvelop;
import com.mealtracker.payloads.me.GetMySettingsResponse;
import com.mealtracker.security.CurrentUser;
import com.mealtracker.services.usersettings.MySettingsInput;
import com.mealtracker.services.usersettings.UserSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/users/me")
public class MeController {

    @Autowired
    private UserSettingsService userSettingsService;

    @GetMapping
    public SuccessEnvelop<GetMySettingsResponse> getMySettings(CurrentUser currentUser) {
        var userSettings = userSettingsService.getUserSettings(currentUser.getId());
        return GetMySettingsResponse.envelop(userSettings);
    }

    @PatchMapping
    public SuccessEnvelop<MessageResponse> updateMySettings(CurrentUser currentUser,
                                                            @Valid @RequestBody MySettingsInput updateRequest) {
        userSettingsService.updateUserSettings(currentUser.getId(), updateRequest);
        return MessageResponse.of("User settings updated successfully");
    }

}
