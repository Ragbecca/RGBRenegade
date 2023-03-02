package com.ragbecca.rgbplayerservice.service;

import com.ragbecca.rgbplayerservice.payload.PlayerRequest;
import org.springframework.http.ResponseEntity;

public interface PlayerService {
    ResponseEntity<?> createPlayer(PlayerRequest playerRequest);
}
