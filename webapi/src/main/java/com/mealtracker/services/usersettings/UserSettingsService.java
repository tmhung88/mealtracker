package com.mealtracker.services.usersettings;

import com.mealtracker.domains.UserSettings;
import com.mealtracker.services.usersettings.UpdateMySettingsInput;
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

    public UserSettings updateUserSettings(long userId, UpdateMySettingsInput input) {
        var user = userRepository.getOne(userId);
        if (input.isDailyCalorieLimitPatched()) {
            user.getUserSettings().setDailyCalorieLimit(input.getDailyCalorieLimit());
        }
        return userRepository.save(user).getUserSettings();
    }
}
