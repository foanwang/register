package com.at.register.security.model;

import com.at.register.constants.CreateType;
import com.at.register.constants.RegisterErrorCode;
import com.at.register.exception.RegisterException;
import com.at.register.exception.RegisterExceptionFactory;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(CreateType.Google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(CreateType.FaceBook.toString())) {
            return new FacebookOAuth2UserInfo(attributes);
        } else {
            throw RegisterExceptionFactory.create(RegisterException.class, RegisterErrorCode.DOES_NOT_SUPPORT_THIS_RESOURCE.getMessage(), RegisterErrorCode.DOES_NOT_SUPPORT_THIS_RESOURCE.getErrorCode());
        }
    }
}
