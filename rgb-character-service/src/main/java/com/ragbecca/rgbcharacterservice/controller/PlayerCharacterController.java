package com.ragbecca.rgbcharacterservice.controller;

import com.ragbecca.rgbcharacterservice.payload.PlayerCharacterRequest;
import com.ragbecca.rgbcharacterservice.service.PlayerCharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/character")
public class PlayerCharacterController {

    @Autowired
    private PlayerCharacterService playerCharacterService;

    @PostMapping("/create")
    public ResponseEntity<?> createCharacter(@RequestBody PlayerCharacterRequest playerCharacterRequest) {
        return playerCharacterService.createCharacter(playerCharacterRequest);
    }

    @GetMapping("/admin/delete")
    public ResponseEntity<?> deleteCharacter(@RequestParam String username) {
        return playerCharacterService.deleteCharacter(username);
    }

    @GetMapping("/races")
    public ResponseEntity<?> getRaces() {
        return playerCharacterService.getRaces();
    }

    @GetMapping("/admin/accept")
    public ResponseEntity<?> verifyUser(@RequestParam String username) {
        return playerCharacterService.acceptCharacter(username);
    }

    @GetMapping("/view/single")
    public ResponseEntity<?> viewSingle(@RequestParam String username) {
        return playerCharacterService.viewSingleCharacter(username);
    }

    @GetMapping("/view/all")
    public ResponseEntity<?> viewAll() {
        return playerCharacterService.viewAllCharacters();
    }

    @GetMapping("/view/all/non-accepted")
    public ResponseEntity<?> viewAllNotAccepted() {
        return playerCharacterService.viewAllNonAcceptedCharacters();
    }

    @GetMapping("/view/all/accepted")
    public ResponseEntity<?> viewAllAccepted() {
        return playerCharacterService.viewAllAcceptedCharacters();
    }
}
