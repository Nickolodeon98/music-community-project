package com.content_i_like.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
public class Recommend extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendNo;

    private String recommendTitle;

    private String recommendContent;

    private String recommendImageUrl;

    private String recommendYoutubeUrl;

    private Long recommendPoint;

    private Long recommendViews;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_no")
    @JsonIgnore
    private Song song;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    @JsonIgnore
    private Member member;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recommend")
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recommend")
    private List<Likes> likes;
}
