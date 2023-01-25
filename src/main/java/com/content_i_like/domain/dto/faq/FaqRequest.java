package com.content_i_like.domain.dto.faq;

import com.content_i_like.domain.entity.FAQ;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FaqRequest {
    private String title;
    private String content;
    private String category;

    public FAQ toEntity(){
        return FAQ.builder()
                .faqTitle(this.title)
                .faqContent(this.content)
                .faqCategory(this.category)
                .build();
    }
}
