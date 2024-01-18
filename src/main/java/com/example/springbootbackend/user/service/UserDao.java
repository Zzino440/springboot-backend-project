package com.example.springbootbackend.user.service;


import com.example.springbootbackend.user.model.User;

import java.util.List;

public interface UserDao {

    List<User> getAllUsers();

    User createUser(User user);

    User getUserById(Long id);

    User updateUser(Long id, User userDetails);

    void deleteUser (Long id);

}
