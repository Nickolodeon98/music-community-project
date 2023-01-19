package com.content_i_like.domain.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private Song song;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;
}
