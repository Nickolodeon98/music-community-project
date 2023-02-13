package com.content_i_like.domain.dto.follow;

import com.content_i_like.domain.entity.Follow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class FollowResponse {
  private Long memberNo;
  private String memberNickName;
  private String profileImg;
  private boolean isFollowBack;

  public FollowResponse(Long memberNo, String memberNickName, String profileImg) {
    this.memberNo = memberNo;
    this.memberNickName = memberNickName;
    this.profileImg = profileImg;
  }
}
