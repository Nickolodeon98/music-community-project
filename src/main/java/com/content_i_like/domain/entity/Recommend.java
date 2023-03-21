package com.content_i_like.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@SQLDelete(sql = "UPDATE recommend SET deleted_at = current_timestamp WHERE recommend_no = ?")
@Where(clause = "deleted_at is null")
@jakarta.persistence.Cacheable
@org.hibernate.annotations.Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class Recommend extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long recommendNo;

  private String recommendTitle;

  @Column(columnDefinition = "TEXT")
  private String recommendContent;

  private String recommendImageUrl;

  private String recommendYoutubeUrl;

  private Long recommendPoint;
  private Long recommendScore; // 모든 포인트를 합산한 점수

  private Long recommendViews;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "new_track_no")
  @JsonIgnore
  private Track track;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_no")
  @JsonIgnore
  private Member member;

  @org.hibernate.annotations.Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "recommend")
  @JsonIgnore
  private List<Comment> comments;

  @org.hibernate.annotations.Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "recommend")
  private List<Likes> likes;

  @org.hibernate.annotations.Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
  @OneToMany(mappedBy = "recommend", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<PostHashtag> postHashtags = new ArrayList<>();
}
