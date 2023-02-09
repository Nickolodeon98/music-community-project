package com.content_i_like.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@SQLDelete(sql = "UPDATE recommend SET deleted_at = current_timestamp WHERE recommend_no = ?")
@Where(clause = "deleted_at is null")
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

  private Long recommendViews;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "track_no")
  @JsonIgnore
  private Track track;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_no")
  @JsonIgnore
  private Member member;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "recommend")
  @JsonIgnore
  private List<Comment> comments;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "recommend")
  private List<Likes> likes;

  @OneToMany(mappedBy = "recommend", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<PostHashtag> postHashtags = new ArrayList<>();
}
