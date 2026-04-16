package com.example.auth.dtos;

import com.example.auth.entities.Provider;
import com.example.auth.entities.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private UUID id;   // represent unique id

    private String email;
    private String name;
    private String password;
    private String image;
    private boolean enable = true;
    private Instant createdAt = Instant.now();
    private Instant updatedAT = Instant.now();

    private Provider provider=Provider.LOCAL;

    private Set<Role> roles = new HashSet<>();

}
