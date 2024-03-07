package com.example.databaseTest;

import com.example.mapper.UserMapper;
import com.example.model.User;
import com.example.model.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserMapper userMapper;
    @Autowired
    public UserController(UserMapper userMapper){
        this.userMapper = userMapper;
    }
    @GetMapping("/allUsers")
    public List<User> findAll(){
        return userMapper.findAll();
    }
    @PostMapping("/insertUser")
    public ResponseEntity<?> insertUser(@RequestBody User newUser){
        userMapper.insertUser(newUser);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/usersById")
    public List<User> findById(@RequestParam(value = "id") int id){
        return userMapper.findById(id);
    }
    @GetMapping("/hello")
    public String helloWorld(){
        return "hello world";
    }
}
