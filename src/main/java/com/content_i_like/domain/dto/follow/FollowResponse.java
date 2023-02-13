package com.content_i_like.domain.dto.follow;

import com.content_i_like.domain.entity.Follow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FollowResponse {
  private Long memberNo;
  private String memberNickName;
  private String profileImg;

}
