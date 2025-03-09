package com.example.cr.user.exception;


public class UserAlreadyExistsException extends CustomException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
