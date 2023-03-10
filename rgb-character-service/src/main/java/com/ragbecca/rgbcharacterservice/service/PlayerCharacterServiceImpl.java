package com.ragbecca.rgbcharacterservice.service;

import com.ragbecca.rgbcharacterservice.model.PlayerCharacter;
import com.ragbecca.rgbcharacterservice.payload.ApiResponse;
import com.ragbecca.rgbcharacterservice.payload.PlayerCharacterRequest;
import com.ragbecca.rgbcharacterservice.repository.PlayerCharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PlayerCharacterServiceImpl implements PlayerCharacterService {
    @Autowired
    private PlayerCharacterRepository playerCharacterRepository;

    public ResponseEntity<?> createCharacter(PlayerCharacterRequest playerCharacterRequest) {
        PlayerCharacter playerCharacter = new PlayerCharacter(playerCharacterRequest);

        playerCharacterRepository.save(playerCharacter);
        return ResponseEntity.ok(new ApiResponse(true, "Character Saved!"));
    }

    @Override
    public ResponseEntity<?> acceptCharacter(String username) {
        PlayerCharacter playerCharacter = playerCharacterRepository.findByUsername(username);
        playerCharacter.setAccepted(true);
        playerCharacterRepository.save(playerCharacter);
        return ResponseEntity.ok(new ApiResponse(true, "Character Accepted!"));
    }

    @Override
    public ResponseEntity<?> getRaces() {
        return ResponseEntity.ok(PlayerCharacter.Race.values());
    }

    @Override
    public ResponseEntity<?> viewSingleCharacter(String username) {
        return ResponseEntity.ok(playerCharacterRepository.findByUsername(username));
    }

    @Override
    public ResponseEntity<?> viewAllNonAcceptedCharacters() {
        return ResponseEntity.ok(playerCharacterRepository.findAllByAccepted(false));
    }

    @Override
    public ResponseEntity<?> viewAllAcceptedCharacters() {
        return ResponseEntity.ok(playerCharacterRepository.findAllByAccepted(true));
    }

    @Override
    public ResponseEntity<?> viewAllCharacters() {
        return ResponseEntity.ok(playerCharacterRepository.findAll());
    }

    @Override
    public ResponseEntity<?> deleteCharacter(String username) {
        playerCharacterRepository.deleteByUsername(username);
        return ResponseEntity.ok(new ApiResponse(true, "Char Deleted"));
    }
}
