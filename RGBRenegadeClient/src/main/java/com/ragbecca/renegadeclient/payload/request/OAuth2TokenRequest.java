package com.ragbecca.renegadeclient.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OAuth2TokenRequest {

    @NotNull
    private String token;

    @NotNull
    private String username;
}
