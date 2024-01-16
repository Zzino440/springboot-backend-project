package com.example.springbootbackend.user.controller;

import com.example.springbootbackend.user.DTO.LoginDTO;
import com.example.springbootbackend.user.DTO.UserDTO;
import com.example.springbootbackend.user.exception.ResourceNotFoundException;
import com.example.springbootbackend.user.model.User;
import com.example.springbootbackend.user.repository.UserRepository;
import com.example.springbootbackend.user.service.UserDao;
import com.example.springbootbackend.utility.LoginMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("api/v1/")
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;
/*
    //save User NEW
    @PostMapping("/users/save")
    public String saveUser(@RequestBody UserDTO userDTO) {
        return userDao.addUser(userDTO);
    }

    //login user NEW
    @PostMapping("users/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDTO){
        LoginMessage loginMessage = userDao.loginUser(loginDTO);
        return ResponseEntity.ok(loginMessage);
    }*/

    //get all users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //create user
    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        try {
            String userJson = objectMapper.writeValueAsString(user);
            log.info("User created: {}", userJson);
        } catch (JsonProcessingException e) {
            log.error("Error during user JSON conversion");
        }
        return userRepository.save(user);
    }

    //get user by id
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserId(@PathVariable Long id) {
        User user = this.findById(id);
        return ResponseEntity.ok(user);
    }

    //update user
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = this.findById(id);

        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());

        User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    //delete user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Long id) {
        User user = this.findById(id);
        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    private User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id:" + id));
    }

}
