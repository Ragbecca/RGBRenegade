package com.ragbecca.rgbcharacterservice.payload;

import com.ragbecca.rgbcharacterservice.model.PlayerCharacter;
import lombok.Data;

@Data
public class PlayerCharacterRequest {
    private String username;
    private String characterName;
    private int age;
    private String description;
    private PlayerCharacter.Race race;
}
