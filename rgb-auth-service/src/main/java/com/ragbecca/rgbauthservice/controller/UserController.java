package com.ragbecca.rgbauthservice.controller;

import com.ragbecca.rgbauthservice.exception.ResourceNotFoundException;
import com.ragbecca.rgbauthservice.models.User;
import com.ragbecca.rgbauthservice.repository.UserRepository;
import com.ragbecca.rgbauthservice.security.CurrentUser;
import com.ragbecca.rgbauthservice.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
}
