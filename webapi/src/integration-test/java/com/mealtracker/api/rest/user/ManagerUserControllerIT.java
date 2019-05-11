package com.mealtracker.api.rest.user;

import com.mealtracker.MealTrackerApplication;
import com.mealtracker.api.rest.user.builders.DomainUserBuilder;
import com.mealtracker.config.WebSecurityConfig;
import com.mealtracker.domains.Role;
import com.mealtracker.services.pagination.PageableOrder;
import com.mealtracker.services.user.DeleteUsersInput;
import com.mealtracker.services.user.ManageUserInput;
import com.mealtracker.services.user.UserService;
import com.mealtracker.services.user.manager.ManagerUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static com.mealtracker.TestError.API_NOT_FOUND;
import static com.mealtracker.TestUser.NO_USER_MANAGEMENT;
import static com.mealtracker.TestUser.USER_MANAGER;
import static com.mealtracker.api.rest.user.matchers.ListUsersInputMatchers.pagination;
import static com.mealtracker.request.AppRequestBuilders.delete;
import static com.mealtracker.request.AppRequestBuilders.get;
import static com.mealtracker.request.AppRequestBuilders.post;
import static com.mealtracker.request.AppRequestBuilders.put;
import static com.mealtracker.utils.MockPageBuilder.oneRowsPerPage;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {ManagerUserController.class})
@ContextConfiguration(classes = {MealTrackerApplication.class, WebSecurityConfig.class})
public class ManagerUserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManagerUserService managerUserService;

    @MockBean
    private UserService userService;

    @Test
    public void addUser_NoUserManagementUser_ExpectApiNotFoundError() throws Exception {
        mockMvc.perform(post("/v1/users").auth(NO_USER_MANAGEMENT).content(manageUserRequest()))
                .andExpect(API_NOT_FOUND.httpStatus());
    }

    @Test
    public void addUser_ValidRequest_ExpectUserAdded() throws Exception {
        mockMvc.perform(post("/v1/users").auth(USER_MANAGER).content(manageUserRequest()))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'message':'User added successfully'}}"));
    }

    @Test
    public void listUsers_NoUserManagementUser_ExpectApiNotFoundError() throws Exception {
        mockMvc.perform(get("/v1/users").auth(NO_USER_MANAGEMENT))
                .andExpect(API_NOT_FOUND.httpStatus());
    }

    @Test
    public void listUsers_ValidRequest_ExpectUsersListed() throws Exception {
        var thor = new DomainUserBuilder()
                .calorieLimit(1500)
                .email("thor@abc.com")
                .fullName("Thor")
                .id(12L).role(Role.REGULAR_USER).build();
        var userPage = oneRowsPerPage(60, thor);
        when(managerUserService.listUsers(pagination("id", PageableOrder.DESC))).thenReturn(userPage);

        mockMvc.perform(get("/v1/users").auth(USER_MANAGER).oneRowPerPage())
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':[{'id':12,'email':'thor@abc.com','fullName':'Thor','role':'REGULAR_USER','dailyCalorieLimit':1500}],'metaData':{'totalElements':60,'totalPages':60}}"));
    }

    @Test
    public void deleteUsers_NoUserManagementUser_ExpectApiNotFoundError() throws Exception {
        mockMvc.perform(delete("/v1/users").auth(NO_USER_MANAGEMENT))
                .andExpect(API_NOT_FOUND.httpStatus());
    }

    @Test
    public void deleteUsers_ValidRequest_ExpectUsersDeleted() throws Exception {
        mockMvc.perform(delete("/v1/users").auth(USER_MANAGER).content(deleteUsersInput(5L, 7L)))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'message':'Users deleted successfully'}}"));
    }

    @Test
    public void getUser_NoUserManagementUser_ExpectApiNotFoundError() throws Exception {
        mockMvc.perform(get("/v1/users/6").auth(NO_USER_MANAGEMENT))
                .andExpect(API_NOT_FOUND.httpStatus());
    }

    @Test
    public void getUser_ValidRequest_ExpectUserDetailsReturned() throws Exception {
        var user = new DomainUserBuilder()
                .calorieLimit(400)
                .email("tombraider@abc.com")
                .fullName("Lara Croft")
                .id(4L).role(Role.REGULAR_USER).build();

        when(managerUserService.getUser(eq(4L))).thenReturn(user);

        mockMvc.perform(get("/v1/users/4").auth(USER_MANAGER))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'id':4,'email':'tombraider@abc.com','fullName':'Lara Croft','role':'REGULAR_USER','dailyCalorieLimit':400}}"));
    }

    @Test
    public void updateUser_NoUserManagementUser_ExpectApiNotFoundError() throws Exception {
        mockMvc.perform(put("/v1/users/6").auth(NO_USER_MANAGEMENT))
                .andExpect(API_NOT_FOUND.httpStatus());
    }

    @Test
    public void updateUser_ValidRequest_ExpectUserDetailsReturned() throws Exception {
        mockMvc.perform(put("/v1/users/2").auth(USER_MANAGER).content(manageUserRequest()))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'message':'User updated successfully'}}"));
    }

    private ManageUserInput manageUserRequest() {
        var input = new ManageUserInput();
        input.setRole(Role.USER_MANAGER.name());
        input.setDailyCalorieLimit(300);
        input.setEmail("ironman@gmail.com");
        input.setFullName("Tony Stark");
        input.setPassword("jarvis");
        return input;
    }

    private DeleteUsersInput deleteUsersInput(Long... userIds) {
        var input = new DeleteUsersInput();
        input.setIds(Arrays.asList(userIds));
        return input;
    }
}
