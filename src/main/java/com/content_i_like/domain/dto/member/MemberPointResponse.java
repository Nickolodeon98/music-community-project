package com.content_i_like.domain.dto.member;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Builder
@AllArgsConstructor
@Data
public class MemberPointResponse {

  private Long totalPoint;
  private Page<PointResponse> pointResponse;
}
