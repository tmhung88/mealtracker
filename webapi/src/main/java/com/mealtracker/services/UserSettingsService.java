package com.mealtracker.services;

import com.mealtracker.domains.UserSettings;
import com.mealtracker.payloads.UpdateMySettingsRequest;
import com.mealtracker.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserSettingsService {

    @Autowired
    private UserRepository userRepository;

    public UserSettings getUserSettings(long userId) {
        var user = userRepository.getOne(userId);
        return user.getUserSettings();
    }

    public UserSettings updateUserSettings(long userId, UpdateMySettingsRequest request) {
        var user = userRepository.getOne(userId);
        if (request.getDailyCalorieLimit() != null) {
            user.getUserSettings().setDailyCalorieLimit(request.getDailyCalorieLimit());
        }
        return userRepository.save(user).getUserSettings();
    }
}
