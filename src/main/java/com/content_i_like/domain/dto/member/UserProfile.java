package com.content_i_like.domain.dto.member;

import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.enums.MemberStatusEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserProfile {
    private final String oauthId;
    private final String name;
    private final String email;

    public Member toMember(){
        return Member.builder()
                .email(this.email)
                .name(this.name)
                .snsCheck(this.oauthId)
                .nickName(this.email)
                .status(MemberStatusEnum.USER)
                .build();
    }
}
