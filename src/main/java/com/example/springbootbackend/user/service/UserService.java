package com.example.springbootbackend.user.service;

import com.example.springbootbackend.user.DTO.UserDTO;
import com.example.springbootbackend.user.mapper.UserMapper;
import com.example.springbootbackend.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserDao userDao;

    public List<UserDTO> getAllUsers() {
        return userDao.getAllUsers().stream()
                .map(UserMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    public User createUser(User user) {
        log.info("Created User {}", user);
        return userDao.createUser(user);
    }

    public UserDTO getUserById(Long id) {
        User user = userDao.getUserById(id);
        return UserMapper.toUserDTO(user);
    }

    public User updateUser(Long id, User user) {
        log.info("Updated User {}", user);
        return userDao.updateUser(id, user);
    }

    public Map<String, Boolean> deleteUser(Long id) {
        userDao.deleteUser(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

}
