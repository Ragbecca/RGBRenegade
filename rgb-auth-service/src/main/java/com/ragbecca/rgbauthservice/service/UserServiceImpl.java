package com.ragbecca.rgbauthservice.service;

import com.ragbecca.rgbauthservice.models.User;
import com.ragbecca.rgbauthservice.payload.ApiResponse;
import com.ragbecca.rgbauthservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<?> changeImageURLUser(String username, String url) {
        if (!userRepository.existsByUsername(username)) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "The username you are using isn't valid"));
        }
        User user = userRepository.findByUsername(username).get();
        user.setImageUrl(url);
        userRepository.save(user);
        return ResponseEntity.ok()
                .body(new ApiResponse(true, "Image URL Chnaged!"));
    }
}
