package com.example.springbootbackend.user.service;


import com.example.springbootbackend.user.model.User;

import java.util.List;

public interface UserDao {

    List<User> getAllUsers();

    List<User> getAllUsersExceptCurrent(Long id);

    User createUser(User user);

    User getUserById(Long id);

    User updateUser(Long id, User userDetails);

    void deleteUser(Long id);

    //utility methods
    Boolean checkEmail(String email);

}
