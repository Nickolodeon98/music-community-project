package com.content_i_like.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class Inquiry extends BaseEntity{
    @Id
    private Long inquiryNo;

    private String inquiryTitle;
    private String inquiryContent;

    @ManyToOne
    @JoinColumn(name = "member_no")
    private Member member;
}
