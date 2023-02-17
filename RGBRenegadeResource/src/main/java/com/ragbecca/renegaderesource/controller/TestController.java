package com.ragbecca.renegaderesource.controller;

import com.ragbecca.renegaderesource.entity.CharacterPlayer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/character")
    public List<CharacterPlayer> getCharacters() {
        List<CharacterPlayer> list = new ArrayList<>();
        list.add(new CharacterPlayer(1L, "TEST"));
        list.add(new CharacterPlayer(2L, "TEST 2"));
        return list;
    }

    @GetMapping("/test2")
    public String getCharactersTest() {
        return "TEST";
    }

    @GetMapping("/test")
    public List<CharacterPlayer> getCharactersTest1() {
        List<CharacterPlayer> list = new ArrayList<>();
        list.add(new CharacterPlayer(1L, "TEST"));
        list.add(new CharacterPlayer(2L, "TEST 2"));
        return list;
    }
}
