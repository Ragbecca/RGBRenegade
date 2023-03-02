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
        PlayerCharacter playerCharacter = new PlayerCharacter(playerCharacterRequest.getUserId(),
                playerCharacterRequest.getCharacterName(),
                playerCharacterRequest.getAge());

        playerCharacterRepository.save(playerCharacter);
        return ResponseEntity.ok(new ApiResponse(true, "Character Saved!"));
    }
}
