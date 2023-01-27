package com.content_i_like.domain.dto.recommend;

import com.content_i_like.domain.entity.Comment;
import com.content_i_like.domain.entity.Recommend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RecommendListResponse {
    private Long recommendNo;
    private String recommendTitle;
    private String recommendImageUrl;
    private String memberNickname;
    private String albumImageUrl;
    private String songTitle;
    private String artistName;
    private String recommendContent;
    private Long countLikes;
    private Long accumulatedPoints;

    public static RecommendListResponse of(Recommend recommend) {
        return RecommendListResponse.builder()
                .recommendNo(recommend.getRecommendNo())
                .recommendTitle(recommend.getRecommendTitle())
                .recommendImageUrl(recommend.getRecommendImageUrl())
                .memberNickname(recommend.getMember().getNickName())
                .albumImageUrl(recommend.getSong().getAlbum().getAlbumImageUrl())
                .songTitle(recommend.getSong().getSongTitle())
                .artistName(recommend.getSong().getAlbum().getArtist().getArtistName())
                .recommendContent(recommend.getRecommendContent())
                .countLikes((long) recommend.getLikes().size())
                .accumulatedPoints(recommend.getComments().stream()
                        .mapToLong(Comment::getCommentPoint)
                        .sum() + recommend.getRecommendPoint())
                .build();
    }
}