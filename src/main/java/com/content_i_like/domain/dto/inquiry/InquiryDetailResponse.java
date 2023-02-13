package com.content_i_like.domain.dto.inquiry;

import com.content_i_like.domain.entity.Inquiry;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class InquiryDetailResponse {

  private Long inquiryNo;
  private String title;
  private String content;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
  private LocalDate createdAt;

  public static InquiryDetailResponse of(Inquiry inquiry) {
    return InquiryDetailResponse.builder()
        .inquiryNo(inquiry.getInquiryNo())
        .title(inquiry.getInquiryTitle())
        .content(inquiry.getInquiryContent())
        .createdAt(inquiry.getCreatedAt().toLocalDate())
        .build();
  }
}
