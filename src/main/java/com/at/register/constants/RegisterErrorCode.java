package com.at.register.constants;

import com.at.register.exception.ServiceResponse;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public enum RegisterErrorCode implements ServiceResponse {
    UNKNOWN(0, "Unknown",HttpStatus.INTERNAL_SERVER_ERROR),
    WRONG_EMAIL_FORMAT(10001, "Email Format is wrong", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_REGISTERED(10002, "The email alreday registered", HttpStatus.BAD_REQUEST),
    EMAIL_CAN_NOT_FOUND(10003, "Email can't find", HttpStatus.BAD_REQUEST),
    PASSWORD_IS_TOO_LESS(10004, "Password length is less than 8", HttpStatus.BAD_REQUEST),
    PASSWORD_IS_NOT_CORRECT(10005, "Password is not correct", HttpStatus.BAD_REQUEST),
    DOES_NOT_SUPPORT_THIS_RESOURCE(10006, "This resouce is not support", HttpStatus.BAD_REQUEST),
    TOKEN_IS_NOT_VALIDED(10007, "Token is not valided", HttpStatus.BAD_REQUEST),
    NOT_FOUND_THE_USER(10008, "User is not found", HttpStatus.BAD_REQUEST),
    UNAUTH_REDIRECT_URI(10009, "Unauthorized Redirect URI ", HttpStatus.BAD_REQUEST);;


    private final static Map<Integer, RegisterErrorCode> code2Error = new HashMap<>();

    static {
        for (RegisterErrorCode se : RegisterErrorCode.values()) {
            code2Error.put(se.errorCode, se);
        }
    }

    /**
     * An integer to identify the error.
     */
    private int errorCode;
    private String message;
    private HttpStatus httpStatus;


    RegisterErrorCode(int errorCode, String message, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    /**
     * Get the error from the error code.
     *
     * @param errorCode The error code.
     * @return a transmit service error.
     */
    public static RegisterErrorCode getFrom(int errorCode) {
        return code2Error.get(errorCode);
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
