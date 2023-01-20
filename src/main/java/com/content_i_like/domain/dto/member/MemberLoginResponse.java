package com.content_i_like.domain.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public class MemberLoginResponse {
    private String jwt;
    private String nickName;
}
