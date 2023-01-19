package com.content_i_like.domain.dto.recommend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RecommendPostResponse {
    private Long recommendNo;
    private String recommendTitle;
    private Long recommendPoint;
}