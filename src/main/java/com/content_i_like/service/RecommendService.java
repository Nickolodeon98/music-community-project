package com.content_i_like.service;

import com.content_i_like.domain.dto.recommend.RecommendPostRequest;
import com.content_i_like.domain.dto.recommend.RecommendResponse;
import org.springframework.stereotype.Service;

@Service
public class RecommendService {
    public RecommendResponse uploadPost(String userEmail, RecommendPostRequest recommendPostRequest) {
        return new RecommendResponse();
    }
}
