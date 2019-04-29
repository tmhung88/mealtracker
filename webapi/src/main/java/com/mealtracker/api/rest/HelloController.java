package com.mealtracker.api.rest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/hello")
public class HelloController {

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public Map<String, String> hello(@PathVariable String name) {
        Map<String, String> response = new HashMap<>();
        response.put("action", "Hello");
        response.put("name", name);
        return response;
    }
}
