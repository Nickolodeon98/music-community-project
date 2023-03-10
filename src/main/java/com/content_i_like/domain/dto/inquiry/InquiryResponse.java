package com.content_i_like.domain.dto.inquiry;

import com.content_i_like.domain.entity.Inquiry;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class InquiryResponse {

  private Long inquiryNo;
  private String title;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
  private LocalDate createdAt;

  private String processingStatus;

  public static InquiryResponse of(Inquiry inquiry) {
    return InquiryResponse.builder()
        .inquiryNo(inquiry.getInquiryNo())
        .title(inquiry.getInquiryTitle())
        .createdAt(inquiry.getCreatedAt().toLocalDate())
        .processingStatus(inquiry.getProcessingStatus())
        .build();
  }
}
