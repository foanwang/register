package com.at.register.security.Oauth;


import com.at.register.constants.RegisterErrorCode;
import com.at.register.entity.User;
import com.at.register.exception.RegisterException;
import com.at.register.exception.RegisterExceptionFactory;
import com.at.register.repository.UserRepository;
import com.at.register.security.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> RegisterExceptionFactory.create(RegisterException.class, RegisterErrorCode.EMAIL_CAN_NOT_FOUND.getMessage(), RegisterErrorCode.EMAIL_CAN_NOT_FOUND.getErrorCode())
        );

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
            () -> RegisterExceptionFactory.create(RegisterException.class, RegisterErrorCode.UNKNOWN.getMessage(), RegisterErrorCode.UNKNOWN.getErrorCode())
        );
        return UserPrincipal.create(user);
    }
}