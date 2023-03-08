package com.ragbecca.rgbauthservice.service;

import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> changeImageURLUser(String username, String url);
}
