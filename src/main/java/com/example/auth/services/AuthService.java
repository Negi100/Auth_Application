package com.example.auth.services;

import com.example.auth.dtos.UserDto;

public interface AuthService {

    UserDto registerUser(UserDto userDto) throws IllegalAccessException;

    //login user


}
