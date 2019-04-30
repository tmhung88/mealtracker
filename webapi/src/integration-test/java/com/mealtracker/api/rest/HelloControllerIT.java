package com.mealtracker.api.rest;

import com.mealtracker.MealTrackerApplication;
import com.mealtracker.config.WebSecurityConfig;
import com.mealtracker.services.HelloService;
import com.mealtracker.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {HelloController.class})
@ContextConfiguration(classes={MealTrackerApplication.class, WebSecurityConfig.class})
public class HelloControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HelloService helloService;

    @MockBean
    private UserService userService;


    @Test
    public void unauth_apitest() throws Exception {
        String name = "hung";
        var response = new HashMap<String, String>();
        response.put("name", name);
        when(helloService.greet(eq(name))).thenReturn(response);

        mockMvc.perform(get("/v1/hello/unauth/hung"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"name\":\"hung\"}"));
    }


    @Test
    public void auth_apitest() throws Exception {
        String name = "hung";
        var response = new HashMap<String, String>();
        response.put("name", name);
        when(helloService.greet(eq(name))).thenReturn(response);

        mockMvc.perform(
                get("/v1/hello/auth/hung")
                        .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJwcml2aWxlZ2VzIjpbIk1ZX01FQUxTIl0sInJvbGUiOiJSRUdVTEFSX1VTRVIiLCJmdWxsTmFtZSI6IlJlZ3VsYXIgVXNlciIsImlkIjozLCJlbWFpbCI6InVzZXJAZ21haWwuY29tIn0.0Z7ny6qmSUbwrF5JfnQmwFqDMw_o_-9uWwFWNdefIugEh_R3H3S3wlyJgIJ9TazMrg2i4ZGA6CjBaPYrEZJxlg")
        )
                .andExpect(status().isOk())
                .andExpect(content().string("{\"name\":\"hung\"}"));
    }
}
