package com.mealtracker.api.rest;

import com.mealtracker.payloads.GetMySettingsResponse;
import com.mealtracker.payloads.MessageResponse;
import com.mealtracker.payloads.SuccessEnvelop;
import com.mealtracker.payloads.UpdateMySettingsRequest;
import com.mealtracker.security.CurrentUser;
import com.mealtracker.services.UserSettingsService;
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
        return GetMySettingsResponse.of(userSettings);
    }

    @PatchMapping
    public SuccessEnvelop<MessageResponse> updateMySettings(CurrentUser currentUser,
                                                            @Valid @RequestBody UpdateMySettingsRequest updateRequest) {
        userSettingsService.updateUserSettings(currentUser.getId(), updateRequest);
        return MessageResponse.of("User settings updated successfully");
    }

}
