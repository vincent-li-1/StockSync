package stocksync.controller;

import stocksync.mapper.UserMapper;
import stocksync.model.User;

import org.apache.tomcat.util.http.parser.MediaType;
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
    @PostMapping(value="/insertUser", consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> insertUser(@RequestBody User newUser) {
        userMapper.insertUser(newUser);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/usersById")
    public List<User> findById(@RequestParam(value = "id") int id){
        return userMapper.findById(id);
    }
    @GetMapping("/hi")
    public String hi(){

        return "HELLO WORLD!";
    }
}
