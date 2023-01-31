package com.content_i_like.domain.dto.faq;

import com.content_i_like.domain.entity.FAQ;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class FaqDetailsResponse {

  private String faqCategory;
  private String faqTitle;
  private String faqContent;

  public static FaqDetailsResponse of(FAQ faq) {
    return FaqDetailsResponse.builder()
        .faqCategory(faq.getFaqCategory())
        .faqContent(faq.getFaqContent())
        .faqTitle(faq.getFaqTitle())
        .build();
  }
}
