package com.content_i_like.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class Inquiry extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long inquiryNo;

  private String inquiryTitle;
  private String inquiryContent;

  @ManyToOne
  @JoinColumn(name = "member_no")
  private Member member;
}
