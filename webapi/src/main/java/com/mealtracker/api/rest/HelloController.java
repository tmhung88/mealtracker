package com.mealtracker.api.rest;

import com.mealtracker.services.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1/hello")
public class HelloController {

    @Autowired
    private HelloService helloService;

    @GetMapping("/unauth/{name}")
    public Map<String, String> helloUnauth(@PathVariable String name) {
        return helloService.greet(name);
    }


    @GetMapping("/auth/{name}")
    public Map<String, String> helloAuth(@PathVariable String name) {
        return helloService.greet(name);
    }
}
