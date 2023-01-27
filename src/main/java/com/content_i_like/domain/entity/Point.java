package com.content_i_like.domain.entity;

import com.content_i_like.domain.enums.PointTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Point extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long pointNo;

  @Enumerated(EnumType.STRING)
  private PointTypeEnum pointType;

  private Long pointExpense;
  private Long pointIncome;

  private Long targetRecommendNo;
  private Long targetCommentNo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_no")
  private Member member;
}
