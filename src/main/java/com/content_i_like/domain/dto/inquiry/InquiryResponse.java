package com.content_i_like.domain.dto.inquiry;

import com.content_i_like.domain.entity.Inquiry;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    private String processingStatus;

    public static InquiryResponse of(Inquiry inquiry) {
        return InquiryResponse.builder()
                .inquiryNo(inquiry.getInquiryNo())
                .title(inquiry.getInquiryTitle())
                .createdAt(inquiry.getCreatedAt())
                .processingStatus("처리중")
                .build();
    }
}
