package com.content_i_like.domain.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberModifyRequest {
    private String profileImgUrl;
    private String introduction;
    private String gender;
    private Integer birth;
    private String newPassword;
    private String verification;
}
