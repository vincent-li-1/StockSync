package com.example.databaseTest;

import com.example.mapper.UserMapper;
import com.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private final UserMapper userMapper;
    @Autowired
    public UserController(UserMapper userMapper){
        this.userMapper = userMapper;
    }
    @GetMapping("/ii")
    public List<User> findAll(){
        return userMapper.findAll();
    }
    @GetMapping("/hello")
    public String helloI(){
        return "hello";
    }
}
