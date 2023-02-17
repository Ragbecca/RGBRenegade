package com.ragbecca.renegadeclient.controller;

import com.ragbecca.renegadeclient.exception.ResourceNotFoundException;
import com.ragbecca.renegadeclient.models.User;
import com.ragbecca.renegadeclient.repository.UserRepository;
import com.ragbecca.renegadeclient.security.CurrentUser;
import com.ragbecca.renegadeclient.security.UserPrincipal;
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
