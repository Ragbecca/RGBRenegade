package com.ragbecca.rgbplayerservice.service;

import com.ragbecca.rgbplayerservice.model.Player;
import com.ragbecca.rgbplayerservice.payload.PlayerRequest;
import com.ragbecca.rgbplayerservice.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class PlayerServiceImpl {

    @Autowired
    private PlayerRepository playerRepository;

    public ResponseEntity<?> createPlayer(PlayerRequest playerRequest) {
        Player player = new Player(playerRequest.getUsername(),
                playerRequest.getName(),
                playerRequest.getImgUrl());

        playerRepository.save(player);
        return ResponseEntity.created(URI.create("/")).body("Player Created!");
    }
}
