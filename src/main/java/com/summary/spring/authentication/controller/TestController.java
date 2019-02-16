package com.summary.spring.authentication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping(value = "/order/{id}")
    public String getOrderId(@PathVariable("id") String id) {
        return String.format("Without authentication, The order id is %s.", id);
    }

    @GetMapping(value = "/item/{id}")
    public String getItemId(@PathVariable("id") String id) {
        return String.format("With authentication, the item id is %s.", id);
    }
}
