package com.example.auth.config;


import com.example.auth.Security.JwtAuthenticationFilter;
import com.example.auth.dtos.ApiError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/v1/auth/register").permitAll()
                                .requestMatchers("/api/v1/auth/login").permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(ex-> ex.authenticationEntryPoint(( request,  response, e)->{
                    //error message
                    e.printStackTrace();
                    response.setStatus(401);
                    response.setContentType("application/json");
                    String message =  e.getMessage();
                    String error= (String) request.getAttribute("error");
                    if(error != null){
                        message = error;
                    }
//                    Map<String,Object> errorMap = Map.of("message",message,"statusCode", 404);
                    var apiError = ApiError.of(HttpStatus.UNAUTHORIZED.value(),"Unauthorized Access", message, request.getRequestURI(),true);
                   var objectMapper = new ObjectMapper();
                    response.getWriter().write(objectMapper.writeValueAsString(apiError));
                }))

        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration){
        return configuration.getAuthenticationManager();
    }
}




//    @Bean
//    public UserDetailsService users(){
//        User.UserBuilder userBuilder = User.withDefaultPasswordEncoder();
//
//        UserDetails user1 = userBuilder.username("Prashant").password("abc").roles("Admin").build();
//        UserDetails user2 = userBuilder.username("Shinam").password("abc").roles("Admin").build();
//        UserDetails user3 = userBuilder.username("Rohit").password("abc").roles("Admin").build();
//
//        return new InMemoryUserDetailsManager(user1,user2,user3);


//    jwt add krne se phele for baise security
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//
//        http.csrf(AbstractHttpConfigurer::disable)
//        .authorizeHttpRequests(auth ->
//                        auth.requestMatchers("/api/v1/auth/register").permitAll()
//                                .requestMatchers("/api/v1/auth/login").permitAll()
//                       .anyRequest().authenticated()
//        )
//                .httpBasic(Customizer.withDefaults());
//
//        return http.build();
//    }