package com.ragbecca.rgbcharacterservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class PlayerCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Setter
    private Long userId;
    @Setter
    private String characterName;
    @Setter
    private int age;

    public PlayerCharacter(Long userId, String characterName, int age) {
        this.userId = userId;
        this.characterName = characterName;
        this.age = age;
    }
}
