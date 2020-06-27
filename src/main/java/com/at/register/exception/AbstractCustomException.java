package com.at.register.exception;

import com.at.register.constants.ApplicationError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

/**
 * AbstractCustomException have common field and method so
 * micro service custom exception need to extend this.
 */
public abstract class AbstractCustomException extends RuntimeException {

    private static final Gson gson = new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .create();

    @Expose
    private final String service;

    @Expose
    private ApplicationError error = new ApplicationError();

    public AbstractCustomException(String service, String message, int code) {
        this.service = service;
        this.getError().setMessage(message);
        this.getError().setCode(code);
    }

    public AbstractCustomException(String service, ServiceResponse responseCode) {
        this(service, responseCode.getMessage(), responseCode.getErrorCode());
    }

    public ApplicationError getError() {
        return error;
    }

    public String toJson() {
        return gson.toJson(this);
    }

    @Override
    public String getMessage() {
        return gson.toJson(this);
    }

}
