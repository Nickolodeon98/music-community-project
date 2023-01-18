package com.content_i_like.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class FAQ {

    @Id
    private Long faqNo;

    private String faqTitle;
    private String faqContent;
    private String faqCategory;
}
