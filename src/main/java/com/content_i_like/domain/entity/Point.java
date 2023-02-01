package com.content_i_like.domain.entity;

import com.content_i_like.domain.dto.member.MemberPointResponse;
import com.content_i_like.domain.dto.member.PointResponse;
import com.content_i_like.domain.enums.PointTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@DynamicInsert
public class Point extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long pointNo;

  @Enumerated(EnumType.STRING)
  private PointTypeEnum pointType;

  private Long pointExpense;
  private Long pointIncome;

  @Column(nullable = false)
  private Long targetRecommendNo;

  @Column(nullable = false)
  private Long targetCommentNo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_no")
  private Member member;

  public PointResponse toResponse() {
    Long pointValue;
    if (this.pointExpense != 0l) {
      pointValue = this.pointExpense * (-1);
    } else {
      pointValue = this.pointIncome;
    }
    return PointResponse.builder()
        .date(this.getCreatedAt())
        .point(pointValue)
        .message(this.getPointType().getMessage())
        .build();
  }
}
