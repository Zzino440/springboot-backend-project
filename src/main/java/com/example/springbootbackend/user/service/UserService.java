package com.example.springbootbackend.user.service;

import com.example.springbootbackend.user.DTO.UserDTO;
import com.example.springbootbackend.user.mapper.UserMapper;
import com.example.springbootbackend.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public List<UserDTO> getAllUsers() {
        return userDao.getAllUsers().stream()
                .map(UserMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO createUser(User user) {
        User savedUser = userDao.createUser(user);
        return UserMapper.toUserDTO(savedUser);
    }

}
