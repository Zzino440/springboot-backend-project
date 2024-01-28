package com.example.springbootbackend.user.service;


import com.example.springbootbackend.user.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserDao {

    List<User> getAllUsers();

    Page<User> getAllUsersExceptCurrent(Long id, int page, int size);

    User createUser(User user);

    User getUserById(Long id);

    User updateUser(Long id, User userDetails);

    void deleteUser(Long id);

    //utility methods
    Boolean checkEmail(String email);

}
