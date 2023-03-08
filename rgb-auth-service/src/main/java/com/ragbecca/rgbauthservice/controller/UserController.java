package com.ragbecca.rgbauthservice.controller;

import com.ragbecca.rgbauthservice.exception.ResourceNotFoundException;
import com.ragbecca.rgbauthservice.models.User;
import com.ragbecca.rgbauthservice.repository.UserRepository;
import com.ragbecca.rgbauthservice.security.CurrentUser;
import com.ragbecca.rgbauthservice.security.UserPrincipal;
import com.ragbecca.rgbauthservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/userInfo")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }

    @GetMapping("/user/image/upload")
    public ResponseEntity<?> changeImageURLUser(@RequestParam String username, @RequestParam String url) {
        return userService.changeImageURLUser(username, url);
    }
}
