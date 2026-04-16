package com.example.auth.exceptions;


import com.example.auth.dtos.ApiError;
import com.example.auth.dtos.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.apache.bcel.classfile.ExceptionTable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            CredentialsExpiredException.class,
            DisabledException.class
    })
    public ResponseEntity<ApiError> handleAuthException(Exception e, HttpServletRequest request){

        var apiError =  ApiError.of(HttpStatus.BAD_REQUEST.value(), "Bad Request" ,e.getMessage(), request.getRequestURI() );
        return ResponseEntity.badRequest().body(apiError);
    }

    //resouce not found exception handler :: method
    @ExceptionHandler(ResouceNotFoundException.class) //
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResouceNotFoundException exception){
        ErrorResponse internalServerError =  new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(internalServerError);
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalAccessException exception){
        ErrorResponse IllegalError = new ErrorResponse(exception.getMessage(),HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(IllegalError);
    }
}
