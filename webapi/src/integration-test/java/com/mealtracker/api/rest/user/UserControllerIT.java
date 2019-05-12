package com.mealtracker.api.rest.user;

import com.mealtracker.MealTrackerApplication;
import com.mealtracker.api.rest.user.builders.DomainUserBuilder;
import com.mealtracker.config.WebSecurityConfig;
import com.mealtracker.domains.Role;
import com.mealtracker.services.pagination.PageableOrder;
import com.mealtracker.services.user.DeleteUsersInput;
import com.mealtracker.services.user.ManageUserInput;
import com.mealtracker.services.user.UserManagementService;
import com.mealtracker.services.user.UserManagementServiceResolver;
import com.mealtracker.services.user.UserService;
import com.mealtracker.utils.matchers.CurrentUserMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static com.mealtracker.TestError.AUTHORIZATION_API_ACCESS_DENIED;
import static com.mealtracker.TestUser.NO_USER_MANAGEMENT;
import static com.mealtracker.TestUser.ONLY_USER_MANAGEMENT;
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
@WebMvcTest(controllers = {UserController.class})
@ContextConfiguration(classes = {MealTrackerApplication.class, WebSecurityConfig.class})
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserManagementServiceResolver serviceResolver;

    @MockBean
    private UserManagementService userManagementService;

    @MockBean
    private UserService userService;

    @Before
    public void setUp() {
        when(serviceResolver.resolve(CurrentUserMatchers.eq(ONLY_USER_MANAGEMENT))).thenReturn(userManagementService);
    }

    @Test
    public void addUser_NoUserManagementUser_ExpectAuthorizationError() throws Exception {
        mockMvc.perform(post("/v1/users").auth(NO_USER_MANAGEMENT).content(manageUserRequest()))
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.httpStatus())
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.json());
    }

    @Test
    public void addUser_NullPassword_ExpectBadInputError() throws Exception {
        var userWithoutPassword = manageUserRequest();
        userWithoutPassword.setPassword(null);

        mockMvc.perform(post("/v1/users").auth(ONLY_USER_MANAGEMENT).content(userWithoutPassword))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{'error':{'code':40000,'message':'Bad Input','errorFields':[{'name':'password','message':'must not be null'}]}}"));
    }

    @Test
    public void addUser_ValidRequest_ExpectUserAdded() throws Exception {
        mockMvc.perform(post("/v1/users").auth(ONLY_USER_MANAGEMENT).content(manageUserRequest()))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'message':'User added successfully'}}"));
    }

    @Test
    public void listUsers_NoUserManagementUser_ExpectAuthorizationError() throws Exception {
        mockMvc.perform(get("/v1/users").auth(NO_USER_MANAGEMENT))
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.httpStatus())
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.json());
    }

    @Test
    public void listUsers_ValidRequest_ExpectUsersListed() throws Exception {
        var hulk = new DomainUserBuilder()
                .calorieLimit(1500)
                .email("hulk@abc.com")
                .fullName("David Banner")
                .id(99L).role(Role.USER_MANAGER).build();
        var userPage = oneRowsPerPage(494, hulk);
        when(userManagementService.listUsers(pagination("id", PageableOrder.DESC))).thenReturn(userPage);

        mockMvc.perform(get("/v1/users").auth(ONLY_USER_MANAGEMENT).oneRowPerPage())
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':[{'id':99,'email':'hulk@abc.com','fullName':'David Banner','role':'USER_MANAGER','dailyCalorieLimit':1500}],'metaData':{'totalElements':494,'totalPages':494}}"));
    }

    @Test
    public void deleteUsers_NoUserManagementUser_ExpectAuthorizationError() throws Exception {
        mockMvc.perform(delete("/v1/users").auth(NO_USER_MANAGEMENT).content(deleteUsersInput(5L, 7L)))
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.httpStatus())
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.json());
    }

    @Test
    public void deleteUsers_BadInput_ExpectBadInputError() throws Exception {
        mockMvc.perform(delete("/v1/users").auth(ONLY_USER_MANAGEMENT).emptyJsonContent())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{'error':{'code':40000,'message':'Bad Input','errorFields':[{'name':'ids','message':'must not be null'}]}}"));
    }

    @Test
    public void deleteUsers_ValidRequest_ExpectUsersDeleted() throws Exception {
        mockMvc.perform(delete("/v1/users").auth(ONLY_USER_MANAGEMENT).content(deleteUsersInput(1L, 2L)))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'message':'Users deleted successfully'}}"));
    }

    @Test
    public void getUser_NoUserManagementUser_ExpectAuthorizationError() throws Exception {
        mockMvc.perform(get("/v1/users/6").auth(NO_USER_MANAGEMENT))
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.httpStatus())
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.json());
    }

    @Test
    public void getUser_ValidRequest_ExpectUserDetailsReturned() throws Exception {
        var user = new DomainUserBuilder()
                .calorieLimit(1500)
                .email("batman@abc.com")
                .fullName("Bruce Wayne")
                .id(15L).role(Role.ADMIN).build();

        when(userManagementService.getUser(eq(15L))).thenReturn(user);
        mockMvc.perform(get("/v1/users/15").auth(ONLY_USER_MANAGEMENT))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'id':15,'email':'batman@abc.com','fullName':'Bruce Wayne','role':'ADMIN','dailyCalorieLimit':1500}}"));
    }

    @Test
    public void updateUser_NoUserManagementUser_ExpectAuthorizationError() throws Exception {
        mockMvc.perform(put("/v1/users/6").auth(NO_USER_MANAGEMENT).content(manageUserRequest()))
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.httpStatus())
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.json());
    }

    @Test
    public void updateUser_BadInput_ExpectBadInputError() throws Exception {
        var badInput = manageUserRequest();
        badInput.setEmail(null);

        mockMvc.perform(put("/v1/users/2").auth(ONLY_USER_MANAGEMENT).content(badInput))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{'error':{'code':40000,'message':'Bad Input','errorFields':[{'name':'email','message':'must not be null'}]}}"));
    }

    @Test
    public void updateUser_ValidRequest_ExpectUserDetailsUpdated() throws Exception {
        mockMvc.perform(put("/v1/users/2").auth(ONLY_USER_MANAGEMENT).content(manageUserRequest()))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'message':'User updated successfully'}}"));
    }

    @Test
    public void updateUser_PasswordOption_ExpectUserDetails() throws Exception {
        var inputWithoutPassword = manageUserRequest();
        inputWithoutPassword.setPassword(null);

        mockMvc.perform(put("/v1/users/2").auth(ONLY_USER_MANAGEMENT).content(inputWithoutPassword))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'message':'User updated successfully'}}"));
    }


    private ManageUserInput manageUserRequest() {
        var input = new ManageUserInput();
        input.setRole(Role.REGULAR_USER.name());
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
