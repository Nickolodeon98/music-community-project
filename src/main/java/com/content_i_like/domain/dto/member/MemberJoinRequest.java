package com.content_i_like.domain.dto.member;

import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.enums.GenderEnum;
import com.content_i_like.domain.enums.MemberStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class MemberJoinRequest {
    private String email;
    private String nickName;
    private String password;
    private String name;
    private GenderEnum gender;
    private String birth;

    public Member toEntity(String password){
        return Member.builder()
                .email(this.email)
                .password(password)
                .gender(this.gender)
                .name(this.name)
                .nickName(this.nickName)
                .birth(this.birth)
                .status(MemberStatusEnum.USER)
                .build();
    }
}
