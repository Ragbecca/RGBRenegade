package com.ragbecca.rgbcharacterservice.service;

import com.ragbecca.rgbcharacterservice.payload.PlayerCharacterRequest;
import org.springframework.http.ResponseEntity;

public interface PlayerCharacterService {

    ResponseEntity<?> createCharacter(PlayerCharacterRequest playerCharacterRequest);
}
