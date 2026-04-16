package com.example.auth.services;

import com.example.auth.dtos.UserDto;
import com.example.auth.entities.Provider;
import com.example.auth.entities.User;
import com.example.auth.exceptions.ResouceNotFoundException;
import com.example.auth.helpers.UserHelper;
import com.example.auth.repositories.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;


    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) throws IllegalAccessException {

        if(userDto.getEmail()==null || userDto.getEmail().isBlank()){
            throw new IllegalAccessException("Email is required");
        }

        if(userRepo.existsByEmail(userDto.getEmail())){
            throw new IllegalAccessException("Email already exists");
        }

        User user = modelMapper.map(userDto, User.class);

        user.setEnable(true);

        user.setProvider(userDto.getProvider()!=null ? userDto.getProvider() : Provider.LOCAL);
        //role assign here to user__for authorization
        //TODO;
        User savedUser = userRepo.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserByEmial(String emial) {

       User user=  userRepo.findByEmail(emial)
                .orElseThrow(() ->new ResouceNotFoundException("User not found with give email ID"));

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        UUID uId = UserHelper.parseUUID(userId);
        User existingUser = userRepo
                .findById(uId)
                .orElseThrow(()  -> new ResouceNotFoundException("User not found with given ID"));
        //we are not goint to change email id for this project
        if(userDto.getName() != null) existingUser.setName(userDto.getName());
        if(userDto.getImage() != null) existingUser.setImage(userDto.getImage());
        if(userDto.getProvider() != null) existingUser.setProvider(userDto.getProvider());
        //TODO: change password updation logic...
        if(userDto.getPassword() != null) existingUser.setPassword(userDto.getPassword());
        existingUser.setEnable(userDto.isEnable());
        User updatedUser = userRepo.save(existingUser);
        return modelMapper.map(updatedUser,UserDto.class);
    }

    @Override
    public void deleteUser(String userId) {
        UUID uId =  UserHelper.parseUUID(userId);
        User user = userRepo.findById(uId).orElseThrow(() -> new ResouceNotFoundException("User not found with given ID"));
        userRepo.delete(user);
    }

    @Override
    public UserDto getUserById(String userId) {
        User user =  userRepo.findById(UserHelper.parseUUID(userId)).orElseThrow(() -> new ResouceNotFoundException("User not found with given ID"));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public Iterable<UserDto> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }
}
