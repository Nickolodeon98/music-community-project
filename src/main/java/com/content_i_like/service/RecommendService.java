package com.content_i_like.service;

import com.content_i_like.domain.dto.recommend.RecommendPostRequest;
import com.content_i_like.domain.dto.recommend.RecommendResponse;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Recommend;
import com.content_i_like.domain.entity.Song;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final MemberRepository memberRepository;
    private final RecommendRepository recommendRepository;

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final SongRepository songRepository;


    public RecommendResponse uploadPost(String userEmail, RecommendPostRequest request) {
        // 글을 작성하는 Member 확인
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> {
                    throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
                });

        // 글과 연결되어 있는 음악 검색
        Song song = songRepository.findById(request.getSongNo())
                .orElseThrow(() -> {
                    throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
                });


        Recommend recommend = recommendRepository.save(RecommendPostRequest.toEntity(request, member, song));
        return new RecommendResponse(recommend.getRecommendNo(), recommend.getRecommendTitle(), recommend.getRecommendPoint());
    }
}
