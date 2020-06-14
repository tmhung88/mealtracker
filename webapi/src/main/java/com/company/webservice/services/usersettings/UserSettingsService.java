package com.company.webservice.services.usersettings;

import com.company.webservice.repositories.UserRepository;
import com.company.webservice.domains.UserSettings;
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
