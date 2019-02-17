package com.summary.spring.authentication.controller;

import com.summary.spring.authentication.Entity.User;
import com.summary.spring.authentication.Model.RegisterDTO;
import com.summary.spring.authentication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/register")
    public User registerNewUser(@RequestBody RegisterDTO user) {
        User newUser = new User(user.getUserName(), user.getPassWord(), user.getPhoneNumber(), user.getEmail());
        return userRepository.save(newUser);
    }

    @GetMapping(value = "/users/{userName}")
    public User getUserByName(@PathVariable(value = "userName") String userName) {
        return userRepository.findByUserName(userName);
    }
}
