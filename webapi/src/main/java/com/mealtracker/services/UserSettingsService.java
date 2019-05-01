package com.mealtracker.services;

import com.mealtracker.domains.UserSettings;
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
}
