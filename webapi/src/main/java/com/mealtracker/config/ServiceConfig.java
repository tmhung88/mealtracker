package com.mealtracker.config;

import com.mealtracker.domains.Role;
import com.mealtracker.services.pagination.PageableBuilder;
import com.mealtracker.services.user.AccessibleRolesUserManagementService;
import com.mealtracker.services.user.UserManagementService;
import com.mealtracker.services.user.UserManagementServiceResolver;
import com.mealtracker.services.user.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.mealtracker.domains.Role.ADMIN;
import static com.mealtracker.domains.Role.REGULAR_USER;
import static com.mealtracker.domains.Role.USER_MANAGER;
import static java.util.Arrays.asList;

@Configuration
public class ServiceConfig {


    @Bean
    public PageableBuilder pageableBuilder() {
        return new PageableBuilder();
    }

    @Bean("managerUserService")
    public UserManagementService managerUserSerivce(UserService userService, PageableBuilder pageableBuilder) {
        return new AccessibleRolesUserManagementService(USER_MANAGER, asList(REGULAR_USER, USER_MANAGER), userService, pageableBuilder);
    }

    @Bean("adminUserService")
    public UserManagementService adminUserSerivce(UserService userService, PageableBuilder pageableBuilder) {
        return new AccessibleRolesUserManagementService(ADMIN, asList(Role.values()), userService, pageableBuilder);
    }

    @Bean
    public UserManagementServiceResolver userMangementServiceResolver(@Qualifier("adminUserService") UserManagementService adminUserService,
                                                                      @Qualifier("managerUserService") UserManagementService managerUserService) {
        return new UserManagementServiceResolver(adminUserService, managerUserService);
    }
}
