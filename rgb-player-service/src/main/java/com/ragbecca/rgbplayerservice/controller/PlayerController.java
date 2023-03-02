package com.ragbecca.rgbplayerservice.controller;

import com.ragbecca.rgbplayerservice.payload.PlayerRequest;
import com.ragbecca.rgbplayerservice.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @PostMapping
    public ResponseEntity<?> createCharacter(@RequestBody PlayerRequest playerRequest) {
        return playerService.createPlayer(playerRequest);
    }
}
