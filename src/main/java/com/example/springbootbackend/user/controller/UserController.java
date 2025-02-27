package com.example.springbootbackend.user.controller;

import com.example.springbootbackend.exceptions.MyProjectError;
import com.example.springbootbackend.exceptions.MyProjectException;
import com.example.springbootbackend.user.DTO.UserCreateUpdateDTO;
import com.example.springbootbackend.user.DTO.UserDTO;
import com.example.springbootbackend.user.mapper.UserMapper;
import com.example.springbootbackend.user.model.User;
import com.example.springbootbackend.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/users/")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class UserController {

    private final UserService userService;

    @GetMapping("/not-current")
    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read')")
    public ResponseEntity<Page<UserDTO>> getAllUsersExceptCurrent(
            @RequestParam Long currentUserId,
            @RequestParam String userEmail,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<UserDTO> users = userService.getAllUsersExceptCurrent(currentUserId, userEmail, page, size);
        return ResponseEntity.ok(users);
    }

    /**
     * This method is used to create a new user.
     * It accepts a User object as a request body and returns a ResponseEntity.
     * The ResponseEntity contains either the created UserDTO object or an error message.
     * This method can only be accessed by users with 'admin:create' authority.
     *
     * @param user This is a User object that is passed in the request body. It contains the details of the user to be created.
     * @return ResponseEntity This returns a ResponseEntity that contains either the created UserDTO object or an error message.
     * @throws ResponseStatusException This exception is thrown when there is an error during the creation of the user.
     */
    @PostMapping("")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateUpdateDTO user) {
        try {
            UserDTO createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (MyProjectException e) {
            // Qui catturi la tua eccezione personalizzata e rispondi con il messaggio di errore
            throw new MyProjectException(MyProjectError.USER_CREATION_GENERIC_ERROR);
        }
    }

    //get user by id
    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = this.userService.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }

    //update user
    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<UserDTO> updateUser(@Valid @PathVariable Long id, @RequestBody UserCreateUpdateDTO userDetails) {
        UserDTO updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    //delete user
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @GetMapping("searchByEmail")
    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read')")
    public ResponseEntity<List<String>> getUserNamesByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.searchUserNamesByEmail(email));
    }

    //test controller that creates an x amount of user determined by numberOfUsers input var
    @PostMapping("generate-users")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<String> generateUsers() {
        userService.generateTestUsers(1000);
        return ResponseEntity.ok("1000 test users generated successfully");
    }

}
