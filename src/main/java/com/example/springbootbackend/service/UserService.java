package com.example.springbootbackend.service;

import com.example.springbootbackend.DTO.LoginDTO;
import com.example.springbootbackend.DTO.UserDTO;
import com.example.springbootbackend.utility.LoginMessage;


public interface UserService {
    String addUser(UserDTO userDTO);

    LoginMessage loginUser(LoginDTO loginDTO);
}
