package com.content_i_like.domain.enums;

import com.content_i_like.domain.dto.member.UserProfile;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
public enum OAuthAttributes {
    GOOGLE("google", (attributes) -> {
        return new UserProfile(
                String.valueOf(attributes.get("sub")),
                (String) attributes.get("name"),
                (String) attributes.get("email")
        );

    }),
    NAVER("naver", (attributes) -> {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return new UserProfile(
                (String) response.get("id"),
                (String) response.get("name"),
                (String) response.get("email")
        );
    });

    private final String registrationId;
    private final Function<Map<String, Object>, UserProfile> of;

    public static UserProfile extract(String registrationId, Map<String,Object> attributes){
        System.out.println("extract 통과");
        return Arrays.stream(values())
                .filter(provider -> registrationId.equals(provider.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of.apply(attributes);
    }
}
