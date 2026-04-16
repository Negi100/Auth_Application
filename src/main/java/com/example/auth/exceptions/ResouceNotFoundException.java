package com.example.auth.exceptions;

public class ResouceNotFoundException extends RuntimeException{

    public ResouceNotFoundException(String message){
        super(message);
    }

    public ResouceNotFoundException(){
        super("Resource not fount !!");
    }
}
