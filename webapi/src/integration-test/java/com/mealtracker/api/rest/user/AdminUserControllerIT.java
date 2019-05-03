package com.mealtracker.api.rest.user;

import com.mealtracker.MealTrackerApplication;
import com.mealtracker.api.rest.user.builders.DomainUserBuilder;
import com.mealtracker.config.WebSecurityConfig;
import com.mealtracker.domains.Role;
import com.mealtracker.services.pagination.PageableOrder;
import com.mealtracker.services.user.DeleteUsersInput;
import com.mealtracker.services.user.ManageUserInput;
import com.mealtracker.services.user.UserService;
import com.mealtracker.services.user.admin.AdminUserService;
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
import static com.mealtracker.TestUser.ADMIN;
import static com.mealtracker.TestUser.NO_USER_MANAGEMENT;
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
@WebMvcTest(controllers = {AdminUserController.class})
@ContextConfiguration(classes = {MealTrackerApplication.class, WebSecurityConfig.class})
public class AdminUserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminUserService adminUserService;

    @MockBean
    private UserService userService;

    @Test
    public void addUser_NoUserManagementUser_ExpectApiNotFoundError() throws Exception {
        mockMvc.perform(post("/v1/users").auth(NO_USER_MANAGEMENT).content(manageUserRequest()))
                .andExpect(API_NOT_FOUND.httpStatus());
    }

    @Test
    public void addUser_ValidRequest_ExpectUserAdded() throws Exception {
        mockMvc.perform(post("/v1/users").auth(ADMIN).content(manageUserRequest()))
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
        var hulk = new DomainUserBuilder()
                .calorieLimit(1500)
                .email("hulk@abc.com")
                .fullName("David Banner")
                .id(99L).role(Role.USER_MANAGER).build();
        var userPage = oneRowsPerPage(494, hulk);
        when(adminUserService.listUsers(pagination("id", PageableOrder.DESC))).thenReturn(userPage);

        mockMvc.perform(get("/v1/users").auth(ADMIN).oneRowPerPage())
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':[{'id':99,'email':'hulk@abc.com','fullName':'David Banner','role':'USER_MANAGER','dailyCalorieLimit':1500}],'metaData':{'totalElements':494,'totalPages':494}}"));
    }

    @Test
    public void deleteUsers_NoUserManagementUser_ExpectApiNotFoundError() throws Exception {
        mockMvc.perform(delete("/v1/users").auth(NO_USER_MANAGEMENT).content(deleteUsersInput(5L, 7L)))
                .andExpect(API_NOT_FOUND.httpStatus());
    }

    @Test
    public void deleteUsers_ValidRequest_ExpectUsersDeleted() throws Exception {
        mockMvc.perform(delete("/v1/users").auth(ADMIN).content(deleteUsersInput(1L, 2L)))
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
                .calorieLimit(1500)
                .email("batman@abc.com")
                .fullName("Bruce Wayne")
                .id(15L).role(Role.ADMIN).build();

        when(adminUserService.getUser(eq(15L))).thenReturn(user);
        mockMvc.perform(get("/v1/users/15").auth(ADMIN))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'id':15,'email':'batman@abc.com','fullName':'Bruce Wayne','role':'ADMIN','dailyCalorieLimit':1500}}"));
    }

    @Test
    public void updateUser_NoUserManagementUser_ExpectApiNotFoundError() throws Exception {
        mockMvc.perform(put("/v1/users/6").auth(NO_USER_MANAGEMENT).content(manageUserRequest()))
                .andExpect(API_NOT_FOUND.httpStatus());
    }

    @Test
    public void updateUser_ValidRequest_ExpectUserDetailsReturned() throws Exception {
        mockMvc.perform(put("/v1/users/2").auth(ADMIN).content(manageUserRequest()))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'message':'User updated successfully'}}"));
    }

    private ManageUserInput manageUserRequest() {
        var input = new ManageUserInput();
        input.setRole(Role.REGULAR_USER);
        input.setDailyCalorieLimit(1000);
        input.setEmail("superman@gmail.com");
        input.setFullName("Clark Kent");
        input.setPassword("HelloWorld");
        return input;
    }

    private DeleteUsersInput deleteUsersInput(Long... userIds) {
        var input = new DeleteUsersInput();
        input.setIds(Arrays.asList(userIds));
        return input;
    }
}
