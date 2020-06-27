package com.at.register.exception;

public class RegisterException extends AbstractCustomException{
    public RegisterException(String service, String message, int code) {
        super(service, message, code);
    }

    public RegisterException(String service, ServiceResponse responseCode) {
        super(service, responseCode);
    }

}
