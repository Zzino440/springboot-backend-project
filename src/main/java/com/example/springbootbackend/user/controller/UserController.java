package com.example.springbootbackend.user.controller;

import com.example.springbootbackend.user.DTO.UserDTO;
import com.example.springbootbackend.user.model.User;
import com.example.springbootbackend.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/users/")
public class UserController {


    private final ObjectMapper objectMapper;

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/not-current")
    public ResponseEntity<Page<UserDTO>> getAllUsersExceptCurrent(
            @RequestParam Long currentUserId,
            @RequestParam String userEmail,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<UserDTO> users = userService.getAllUsersExceptCurrent(currentUserId, userEmail, page, size);
        return ResponseEntity.ok(users);
    }

    //create user
    @PostMapping("")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    //get user by id
    @GetMapping("{id}")
    public ResponseEntity<UserDTO> getUserId(@PathVariable Long id) {
        UserDTO userDTO = this.userService.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }

    //update user
    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@Valid @PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    //delete user
    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @GetMapping("searchByEmail")
    public ResponseEntity<List<String>> getUserNamesByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.searchUserNamesByEmail(email));
    }

    //test controller that creates an x amount of user determined bu numberOfUsers input var
    @PostMapping("generate-users")
    public ResponseEntity<String> generateUsers() {
        userService.generateTestUsers(1000);
        return ResponseEntity.ok("1000 test users generated successfully");
    }

}
