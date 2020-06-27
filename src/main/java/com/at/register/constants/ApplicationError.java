package com.at.register.constants;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

public class ApplicationError extends RuntimeException{

    private static final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    @Expose
    private String message = "An unknown error occurred.";

    @Expose
    private int code;

    public ApplicationError() {

    }

    public ApplicationError(String message) {
        this.message = message;
    }

    public ApplicationError(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toJson() {
        return gson.toJson(this);
    }
}
