package com.company.webservice.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.company.webservice.TestUser;
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

    public AppRequestBuilder content(Object object) {
        if (object instanceof String) {
            builder.content((String) object);
        } else {
            builder.contentType(MediaType.APPLICATION_JSON);
            builder.content(json(object));
        }
        return this;
    }

    public AppRequestBuilder emptyJsonContent() {
        builder.contentType(MediaType.APPLICATION_JSON);
        builder.content("{}");
        return this;
    }

    public AppRequestBuilder pagination(int rowPerPages) {
        builder.param("rowsPerPage", String.valueOf(rowPerPages));
        return this;
    }

    public AppRequestBuilder param(String name, String... values) {
        builder.param(name, values);
        return this;
    }

    public AppRequestBuilder oneRowPerPage() {
        return pagination(1);
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
