package com.ragbecca.rgbcharacterservice.service;

import com.ragbecca.rgbcharacterservice.payload.PlayerCharacterRequest;
import org.springframework.http.ResponseEntity;

public interface PlayerCharacterService {

    ResponseEntity<?> createCharacter(PlayerCharacterRequest playerCharacterRequest);

    ResponseEntity<?> acceptCharacter(String username);

    ResponseEntity<?> getRaces();

    ResponseEntity<?> viewSingleCharacter(String username);

    ResponseEntity<?> viewAllNonAcceptedCharacters();

    ResponseEntity<?> viewAllAcceptedCharacters();

    ResponseEntity<?> viewAllCharacters();

    ResponseEntity<?> deleteCharacter(String username);
}
