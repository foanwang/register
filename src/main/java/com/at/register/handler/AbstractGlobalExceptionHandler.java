package com.at.register.handler;


import com.at.register.exception.RegisterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * AbstractGlobalExceptionHandler handle Spring Boot default exception so
 * micro service global exception handler need to extend this.
 */
@Slf4j
public abstract class AbstractGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String FIELD = "field";
    private static final String MESSAGE = "message";

    @Value("${info.service-name}")
    private String serviceName;

    private Gson gson = new GsonBuilder().create();

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<String> handleApplicationException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {

        log.info(ex.getMessage(), ex);
        return getObjectResponseEntity(ex.getMessage(), 9999, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getMessage(), ex);
        return getObjectResponseEntity(ex.getMessage(), 9999, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
                                                                      HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getMessage(), ex);
        return getObjectResponseEntity(ex.getMessage(), 9999, status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex,
                                                               HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getMessage(), ex);
        return getObjectResponseEntity(ex.getMessage(), 9999, status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getMessage(), ex);
        return getObjectResponseEntity(ex.getMessage(), 9999, status);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
                                                                          HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getMessage(), ex);
        return getObjectResponseEntity(ex.getMessage(), 9999, status);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getMessage(), ex);
        return getObjectResponseEntity(ex.getMessage(), 9999, status);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex,
                                                        HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getMessage(), ex);
        return getObjectResponseEntity(ex.getMessage(), 9999, status);
    }
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getMessage(), ex);
        return getObjectResponseEntity(ex.getMessage(), 9999, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getMessage(), ex);
        return getObjectResponseEntity(ex.getMessage(), 9999, status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
                                                                     HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getMessage(), ex);
        return getObjectResponseEntity(ex.getMessage(), 9999, status);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getMessage(), ex);
        return getObjectResponseEntity(ex.getMessage(), 9999, status);
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex,
                                                                        HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getMessage(), ex);
        return getObjectResponseEntity(ex.getMessage(), 9999, status);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(
            BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        System.out.println(this.serviceName);
        return getObjectResponseEntity(ex.getBindingResult(), 9998, status);
    }

    //Pattern
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return getObjectResponseEntity(ex.getBindingResult(), 9998, status);
    }

    private ResponseEntity<Object> getObjectResponseEntity(String message, int code, HttpStatus status) {
        return createResponseEntity(message, code, status);
    }

    private ResponseEntity<Object> getObjectResponseEntity(BindingResult bindingResult, int code, HttpStatus status) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<JsonObject> fields = new ArrayList<>();

        for (FieldError fieldError : fieldErrors) {
            JsonObject field = new JsonObject();
            field.addProperty(FIELD, fieldError.getField());
            field.addProperty(MESSAGE, fieldError.getDefaultMessage());
            fields.add(field);
        }

        String message = gson.toJson(fields);

        return createResponseEntity(message, code, status);
    }

    private ResponseEntity<Object> createResponseEntity(String message, int code, HttpStatus status) {
        RegisterException clientErrorException = new RegisterException(serviceName, message, code);
        return new ResponseEntity<>(clientErrorException.toJson(), status);
    }

}
