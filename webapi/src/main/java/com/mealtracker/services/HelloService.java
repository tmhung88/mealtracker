package com.mealtracker.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class HelloService {

    public Map<String, String> greet(String name) {
        Map<String, String> response = new HashMap<>();
        response.put("action", "Hello");
        response.put("name", name);
        return response;
    }
}
