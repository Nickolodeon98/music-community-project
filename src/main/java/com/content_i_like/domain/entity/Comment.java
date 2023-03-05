package com.content_i_like.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@SQLDelete(sql = "UPDATE comment SET deleted_at = current_timestamp WHERE comment_no = ?")
@Where(clause = "deleted_at is null")
@jakarta.persistence.Cacheable
@org.hibernate.annotations.Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class Comment extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long commentNo;

  private String commentContent;

  private Long commentPoint;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_no")
  @JsonIgnore
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "recommend_no")
  private Recommend recommend;
}
