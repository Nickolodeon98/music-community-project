package com.content_i_like.domain.dto.search;

import com.content_i_like.domain.entity.Member;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchMembersResponse {

  private String nickName;
  private String profileImgUrl;
  private LocalDateTime createdAt;

  public static SearchMembersResponse of(Member member) {
    return SearchMembersResponse
        .builder()
        .nickName(member.getNickName())
        .profileImgUrl(member.getProfileImgUrl())
        .createdAt(member.getCreatedAt())
        .build();
  }
}
