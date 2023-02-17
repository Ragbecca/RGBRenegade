package com.ragbecca.renegadeclient.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private Long id;
    private String username;
    private String name;
    private String imgUrl;
    private List<String> roles;

    public JwtResponse(String accessToken, String refreshToken, Long id, String username, String name, String imgUrl, List<String> roles) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.name = name;
        this.imgUrl = imgUrl;
        this.roles = roles;
    }
}
