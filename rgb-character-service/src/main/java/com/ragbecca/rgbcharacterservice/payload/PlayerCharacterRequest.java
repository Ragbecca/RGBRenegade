package com.ragbecca.rgbcharacterservice.payload;

import lombok.Data;

@Data
public class PlayerCharacterRequest {
    private long userId;
    private String characterName;
    private int age;

}
