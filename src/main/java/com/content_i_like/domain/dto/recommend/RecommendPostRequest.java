package com.content_i_like.domain.dto.recommend;

import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Recommend;
import com.content_i_like.domain.entity.Song;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RecommendPostRequest {
    private String recommendTitle;
    private String recommendContent;
    private String recommendImageUrl;
    private String recommendYoutubeUrl;
    private Long recommendPoint;
    private Long songNo;

    public static Recommend toEntity(RecommendPostRequest request, Member member, Song song) {
        return Recommend.builder()
                .recommendTitle(request.getRecommendTitle())
                .recommendContent(request.getRecommendContent())
                .recommendImageUrl(request.getRecommendImageUrl())
                .recommendYoutubeUrl(request.getRecommendYoutubeUrl())
                .recommendPoint(request.getRecommendPoint())
                .recommendViews(0L)
                .member(member)
                .song(song)
                .build();
    }
}
