package com.at.register.handler;

import com.at.register.exception.ApplicationException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CustomResponseErrorHandler implements ResponseErrorHandler {

    private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

    private JsonParser parser = new JsonParser();

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return errorHandler.hasError(response);
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        String theString = ResponseBodyToStr(response);
        HttpStatus statusCode = response.getStatusCode();

        //  System.out.println("StatusCode :" + statusCode);
        JsonObject jsonObject = (JsonObject) parser.parse(theString);
        int code = jsonObject.get("error").getAsJsonObject().get("code").getAsInt();
        String message = jsonObject.get("error").getAsJsonObject().get("message").getAsString();
        throw new ApplicationException(message, code);
    }

    private String ResponseBodyToStr(ClientHttpResponse response) {
        return new String(getResponseBody(response), StandardCharsets.UTF_8);
    }


    private byte[] getResponseBody(ClientHttpResponse response) {
        try {
            return FileCopyUtils.copyToByteArray(response.getBody());
        } catch (IOException var3) {
            return new byte[0];
        }
    }

}
