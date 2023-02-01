package com.content_i_like.domain.dto.member;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class MemberPointResponse {

  private Long totalPoint;
  private List<PointResponse> pointResponse;
}
