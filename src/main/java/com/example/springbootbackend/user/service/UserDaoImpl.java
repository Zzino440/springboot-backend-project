package com.example.springbootbackend.user.service;

import com.example.springbootbackend.user.model.User;
import com.example.springbootbackend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDaoImpl implements UserDao {

    //injections
    @Autowired
    private UserRepository userRepository;

    //methods implemented
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }
}
