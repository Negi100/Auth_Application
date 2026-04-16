package com.example.auth.Security;

import com.example.auth.entities.User;
import com.example.auth.exceptions.ResouceNotFoundException;
import com.example.auth.repositories.UserRepo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return   userRepo.findByEmail(username).orElseThrow(()-> new ResouceNotFoundException("Invalid Email or Password !!"));

    }



}
