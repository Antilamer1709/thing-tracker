package com.antilamer.thingTracker.security.oauth2;

import com.antilamer.thingTracker.enums.UserRole;
import com.antilamer.thingTracker.exception.OAuth2AuthenticationProcessingException;
import com.antilamer.thingTracker.model.UserEntity;
import com.antilamer.thingTracker.repository.UserRepo;
import com.antilamer.thingTracker.security.oauth2.user.OAuth2UserInfo;
import com.antilamer.thingTracker.security.oauth2.user.OAuth2UserInfoFactory;
import com.antilamer.thingTracker.service.AuthenticationBO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepo userRepository;

    private final AuthenticationBO authenticationBO;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        log.debug("*** loadUser() oAuth2UserRequest: " + oAuth2UserRequest);
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
        log.debug("*** processOAuth2User() oAuth2UserRequest: " + oAuth2UserRequest);
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        UserEntity user = userRepository.findByEmailIgnoreCase(oAuth2UserInfo.getEmail());
        if(user != null) {
//            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
//                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
//                        user.getProvider() + " account. Please use your " + user.getProvider() +
//                        " account to login.");
//            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return user;
    }

    private UserEntity registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        log.debug("*** registerNewUser() oAuth2UserRequest: " + oAuth2UserRequest);
        UserEntity user = new UserEntity();

        user.setFullName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        authenticationBO.addUserRoles(user, UserRole.USER);
        return userRepository.save(user);
    }

    private UserEntity updateExistingUser(UserEntity existingUser, OAuth2UserInfo oAuth2UserInfo) {
        log.debug("*** updateExistingUser() oAuth2UserInfo: " + oAuth2UserInfo);
        existingUser.setFullName(oAuth2UserInfo.getName());
        return userRepository.save(existingUser);
    }

}
