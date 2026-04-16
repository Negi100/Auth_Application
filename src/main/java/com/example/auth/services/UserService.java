package com.example.auth.services;

import com.example.auth.dtos.UserDto;
import lombok.NoArgsConstructor;


public interface UserService {

    // create user
    UserDto createUser(UserDto userDto) throws IllegalAccessException;

    //get user by email
    UserDto getUserByEmial(String emial);

    //update user
    UserDto updateUser(UserDto userDto, String userId );

    //delete user
    void deleteUser(String userId);

    //get user by id
    UserDto getUserById(String userId);

    //get all user
    Iterable<UserDto> getAllUsers();
}
