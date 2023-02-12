package com.content_i_like.domain.dto.inquiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InquiryRequire {

  private String title;
  private String content;
}
