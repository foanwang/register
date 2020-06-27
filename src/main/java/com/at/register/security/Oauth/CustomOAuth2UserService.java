package com.at.register.security.Oauth;

import com.at.register.exception.RegisterExceptionFactory;
import com.at.register.constants.CreateType;
import com.at.register.constants.RegisterErrorCode;
import com.at.register.entity.User;
import com.at.register.exception.RegisterException;
import com.at.register.repository.UserRepository;
import com.at.register.security.model.UserPrincipal;
import com.at.register.security.model.OAuth2UserInfo;
import com.at.register.security.model.OAuth2UserInfoFactory;
import com.at.register.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw RegisterExceptionFactory.create(RegisterException.class, RegisterErrorCode.EMAIL_CAN_NOT_FOUND.getMessage(), RegisterErrorCode.EMAIL_CAN_NOT_FOUND.getErrorCode());

        }

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if(!user.getCreateType().equals(CreateType.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw RegisterExceptionFactory.create(RegisterException.class, RegisterErrorCode.DOES_NOT_SUPPORT_THIS_RESOURCE.getMessage(), RegisterErrorCode.DOES_NOT_SUPPORT_THIS_RESOURCE.getErrorCode());
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        return userService.userRegister(oAuth2UserInfo.getEmail(),oAuth2UserInfo.getUserName(), "",CreateType.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setUsername(oAuth2UserInfo.getUserName());
        return userRepository.save(existingUser);
    }

}
