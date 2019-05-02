package com.mealtracker.api.rest.user;

import com.mealtracker.MealTrackerApplication;
import com.mealtracker.config.WebSecurityConfig;
import com.mealtracker.services.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.mealtracker.TestError.AUTHORIZATION_API_ACCESS_DENIED;
import static com.mealtracker.TestUser.NO_USER_MANAGEMENT;
import static com.mealtracker.TestUser.USER_MANAGER;
import static com.mealtracker.request.AppRequestBuilders.delete;
import static com.mealtracker.request.AppRequestBuilders.get;
import static com.mealtracker.request.AppRequestBuilders.post;
import static com.mealtracker.request.AppRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {ManagerUserController.class})
@ContextConfiguration(classes = {MealTrackerApplication.class, WebSecurityConfig.class})
public class ManagerUserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void addUser_NoUserManagementUser_ExpectAuthorizationError() throws Exception {
        mockMvc.perform(post("/v1/users").auth(NO_USER_MANAGEMENT))
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.httpStatus())
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.json());
    }

    @Test
    public void addUser_ValidRequest_ExpectUserAdded() throws Exception {
        mockMvc.perform(post("/v1/users").auth(USER_MANAGER))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'message':'Manager wants to add a new user'}}"));
    }

    @Test
    public void listUsers_NoUserManagementUser_ExpectAuthorizationError() throws Exception {
        mockMvc.perform(get("/v1/users").auth(NO_USER_MANAGEMENT))
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.httpStatus())
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.json());
    }

    @Test
    public void listUsers_ValidRequest_ExpectUsersListed() throws Exception {
        mockMvc.perform(get("/v1/users").auth(USER_MANAGER))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'message':'Manager wants to list users'}}"));
    }

    @Test
    public void deleteUsers_NoUserManagementUser_ExpectAuthorizationError() throws Exception {
        mockMvc.perform(delete("/v1/users").auth(NO_USER_MANAGEMENT))
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.httpStatus())
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.json());
    }

    @Test
    public void deleteUsers_ValidRequest_ExpectUsersDeleted() throws Exception {
        mockMvc.perform(delete("/v1/users").auth(USER_MANAGER))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'message':'Manager wants to delete users'}}"));
    }

    @Test
    public void getUser_NoUserManagementUser_ExpectAuthorizationError() throws Exception {
        mockMvc.perform(get("/v1/users/6").auth(NO_USER_MANAGEMENT))
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.httpStatus())
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.json());
    }

    @Test
    public void getUser_ValidRequest_ExpectUserDetailsReturned() throws Exception {
        mockMvc.perform(get("/v1/users/15").auth(USER_MANAGER))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'message':'Manager wants to get details of user 15'}}"));
    }

    @Test
    public void updateUser_NoUserManagementUser_ExpectAuthorizationError() throws Exception {
        mockMvc.perform(put("/v1/users/6").auth(NO_USER_MANAGEMENT))
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.httpStatus())
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.json());
    }

    @Test
    public void updateUser_ValidRequest_ExpectUserDetailsReturned() throws Exception {
        mockMvc.perform(put("/v1/users/2").auth(USER_MANAGER))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'message':'Manager wants to update user 2'}}"));
    }
}
