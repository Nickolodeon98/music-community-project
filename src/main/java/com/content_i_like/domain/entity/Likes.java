package com.content_i_like.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@SQLDelete(sql = "UPDATE likes SET deleted_at = current_timestamp WHERE likes_no = ?")
public class Likes extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long likesNo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_no")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "recommend_no")
  private Recommend recommend;

  public static Likes toEntity(Recommend post, Member member) {
    return Likes.builder()
        .recommend(post)
        .member(member)
        .build();
  }
}
