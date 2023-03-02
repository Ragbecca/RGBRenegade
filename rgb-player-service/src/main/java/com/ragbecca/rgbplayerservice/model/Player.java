package com.ragbecca.rgbplayerservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Setter
    private String username;
    @Setter
    private String name;
    @Setter
    private String imgUrl;

    public Player(String username, String name, String imgUrl) {
        this.username = username;
        this.name = name;
        this.imgUrl = imgUrl;
    }
}
