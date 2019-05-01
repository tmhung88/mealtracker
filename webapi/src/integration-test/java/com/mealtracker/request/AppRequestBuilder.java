package com.mealtracker.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealtracker.TestUser;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.servlet.ServletContext;

public class AppRequestBuilder implements RequestBuilder {


    private MockHttpServletRequestBuilder builder;

    public AppRequestBuilder(MockHttpServletRequestBuilder builder) {
        this.builder = builder;
        this.builder.characterEncoding("utf-8");
    }

    @Override
    public MockHttpServletRequest buildRequest(ServletContext servletContext) {
        return builder.buildRequest(servletContext);
    }

    public AppRequestBuilder auth(TestUser user) {
        builder.header("Authorization", user.getToken());
        return this;
    }

    public AppRequestBuilder content(Object object) {
        if (object instanceof String) {
            this.builder.content((String) object);
        } else {
            this.builder.contentType(MediaType.APPLICATION_JSON);
            this.builder.content(json(object));
        }
        return this;
    }

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static String json(Object object)  {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
