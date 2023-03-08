package com.ragbecca.rgbauthservice.security.oauth2;

import com.ragbecca.rgbauthservice.exception.OAuth2AuthenticationProcessingException;
import com.ragbecca.rgbauthservice.models.AuthProvider;
import com.ragbecca.rgbauthservice.models.User;
import com.ragbecca.rgbauthservice.repository.UserRepository;
import com.ragbecca.rgbauthservice.security.UserPrincipal;
import com.ragbecca.rgbauthservice.security.oauth2.user.OAuth2UserInfo;
import com.ragbecca.rgbauthservice.security.oauth2.user.OAuth2UserInfoFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class RGBOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

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
        if (oAuth2UserInfo.getEmail().isEmpty() &&
                !(Objects.equals(oAuth2UserRequest.getClientRegistration().getClientName().toLowerCase(), AuthProvider.github.name()))) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByUsername(oAuth2UserInfo.getEmail() == null
                ? oAuth2User.getAttributes().get("login").toString() : oAuth2UserInfo.getEmail());
        User user;

        // TODO: SPLIT
        if (userOptional.isPresent()) {
            user = userOptional.get();
            // TODO user provider fix
            if (!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
            if (oAuth2UserInfo.getName() == null) {
                GithubUserRequest githubUserRequest = new GithubUserRequest(oAuth2User.getAttributes().get("login").toString(), oAuth2UserInfo.getImageUrl());
                user = updateExistingUserWithCustomName(user, githubUserRequest);
            } else {
                user = updateExistingUser(user, oAuth2UserInfo);
            }
        } else {
            if (oAuth2UserInfo.getEmail().isEmpty()) {
                GithubUserRequestRegister githubUserRequest = new GithubUserRequestRegister(oAuth2User.getAttributes().get("login").toString());
                user = registerNewUserGithub(oAuth2UserRequest, oAuth2UserInfo, githubUserRequest);
            } else {
                user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
            }
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    // TODO CREATE HELPER INSTEAD OF DUPLICATE CODE
    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();

        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setName(oAuth2UserInfo.getName());
        user.setUsername(oAuth2UserInfo.getEmail());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        user.setEmailVerified(true);
        user.setRole("USER");
        return userRepository.save(user);
    }

    private User registerNewUserGithub(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo, GithubUserRequestRegister githubUserRequest) {
        User user = new User();

        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setName(githubUserRequest.getUsername());
        user.setUsername(githubUserRequest.getUsername());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        user.setEmailVerified(true);
        user.setRole("USER");
        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setName(oAuth2UserInfo.getName());
        existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }

    private User updateExistingUserWithCustomName(User existingUser, GithubUserRequest githubUserRequest) {
        existingUser.setName(githubUserRequest.getName());
        existingUser.setImageUrl(githubUserRequest.getImageUrl());
        return userRepository.save(existingUser);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class GithubUserRequestRegister {
        private String username;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class GithubUserRequest {
        private String name;
        private String imageUrl;
    }

}
