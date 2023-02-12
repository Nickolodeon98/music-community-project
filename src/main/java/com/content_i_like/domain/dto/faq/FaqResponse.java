package com.content_i_like.domain.dto.faq;

import com.content_i_like.domain.entity.FAQ;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaqResponse {

  private Long faqNo;
  private String faqCategory;
  private String title;

  public static FaqResponse of(FAQ faq) {
    return FaqResponse.builder()
        .faqNo(faq.getFaqNo())
        .faqCategory(faq.getFaqCategory())
        .title(faq.getFaqTitle())
        .build();
  }
}
