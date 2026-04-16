package com.example.auth.controllers;

import com.example.auth.dtos.UserDto;
import com.example.auth.entities.User;
import com.example.auth.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }
    //
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) throws IllegalAccessException{
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));
    }

    //get all user api
    @GetMapping
    public ResponseEntity<Iterable<UserDto>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    //get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
        return ResponseEntity.ok(userService.getUserByEmial(email));
    }

    //get user by id
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    //delete user
    @DeleteMapping("/{userId}")
    public void delelUserById(@PathVariable String userId){
        userService.deleteUser(userId);
    }

    //update user
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, @PathVariable String userId){
        return ResponseEntity.ok(userService.updateUser(userDto,userId));
    }

}
