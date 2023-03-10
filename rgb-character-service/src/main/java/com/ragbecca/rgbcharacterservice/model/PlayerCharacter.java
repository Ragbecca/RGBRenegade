package com.ragbecca.rgbcharacterservice.model;

import com.ragbecca.rgbcharacterservice.payload.PlayerCharacterRequest;
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
    @Column(name = "username", nullable = false)
    private String username;
    @Setter
    @Column(name = "characterName", nullable = false)
    private String characterName;
    @Setter
    @Column(name = "age", nullable = false)
    private int age;
    @Setter
    @Column(name = "characterDescription", nullable = false)
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "race", length = 16, nullable = false)
    @Setter
    private Race race;
    @Setter
    @Column(name = "isAccepted", nullable = false)
    private boolean accepted = false;

    public PlayerCharacter(String username, String characterName, int age, String description, Race race,
                           boolean accepted) {
        this.username = username;
        this.characterName = characterName;
        this.age = age;
        this.description = description;
        this.race = race;
        this.accepted = accepted;
    }

    public PlayerCharacter(PlayerCharacterRequest playerCharacterRequest) {
        this.username = playerCharacterRequest.getUsername();
        this.characterName = playerCharacterRequest.getCharacterName();
        this.age = playerCharacterRequest.getAge();
        this.description = playerCharacterRequest.getDescription();
        this.race = playerCharacterRequest.getRace();
    }

    /**
     * Enum containing valid races.
     */
    public enum Race {
        ELF, HUMAN, ORC, BEAST_MEN;

        public String asString() {
            return name();
        }
    }
}
