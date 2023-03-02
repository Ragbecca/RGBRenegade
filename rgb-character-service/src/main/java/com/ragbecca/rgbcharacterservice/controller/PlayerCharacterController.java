package com.ragbecca.rgbcharacterservice.controller;

import com.ragbecca.rgbcharacterservice.payload.PlayerCharacterRequest;
import com.ragbecca.rgbcharacterservice.service.PlayerCharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/character")
public class PlayerCharacterController {

    @Autowired
    private PlayerCharacterService playerCharacterService;

    @PostMapping
    public ResponseEntity<?> createCharacter(@RequestBody PlayerCharacterRequest playerCharacterRequest) {
        return playerCharacterService.createCharacter(playerCharacterRequest);
    }
}
