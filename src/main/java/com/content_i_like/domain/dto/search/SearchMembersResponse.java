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

  private Long memberNo;
  private String nickName;
  private String profileImgUrl;
  private LocalDateTime createdAt;

  public static SearchMembersResponse of(Member member) {
    return SearchMembersResponse
        .builder()
        .memberNo(member.getMemberNo())
        .nickName(summarize(member.getNickName(), 8))
        .profileImgUrl(member.getProfileImgUrl())
        .createdAt(member.getCreatedAt())
        .build();
  }

  public static String summarize(String text, int length) {
    return text.length() > length ? String.format("%s...", text.substring(0, length)) : text;
  }
}