package com.content_i_like.domain.enums;

import lombok.Getter;
import lombok.Value;

@Getter
public enum TrackEnum {
    GRANT_TYPE("authorization_code"),
    REDIRECT_URI("http://localhost:8080/api/v1/test"),
    BASE_URL("https://api.spotify.com/v1"),
    TOKEN_URL("https://accounts.spotify.com/api/token");

    private final String value;

    TrackEnum(String value) {
        this.value = value;
    }
}
