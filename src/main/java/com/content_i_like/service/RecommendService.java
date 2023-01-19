package com.content_i_like.service;

import com.content_i_like.domain.dto.recommend.RecommendModifyRequest;
import com.content_i_like.domain.dto.recommend.RecommendModifyResponse;
import com.content_i_like.domain.dto.recommend.RecommendPostRequest;
import com.content_i_like.domain.dto.recommend.RecommendPostResponse;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Recommend;
import com.content_i_like.domain.entity.Song;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final MemberRepository memberRepository;
    private final RecommendRepository recommendRepository;

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final SongRepository songRepository;


    @Transactional
    public RecommendPostResponse uploadPost(String userEmail, RecommendPostRequest request) {
        // 글을 작성하는 Member 확인
        Member member = validateGetMemberInfoByUserEmail(userEmail);

        // 글과 연결되어 있는 음악 검색
        Song song = songRepository.findById(request.getSongNo())
                .orElseThrow(() -> {
                    throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
                });


        Recommend recommend = recommendRepository.save(RecommendPostRequest.toEntity(request, member, song));
        return new RecommendPostResponse(recommend.getRecommendNo(), recommend.getRecommendTitle(), recommend.getRecommendPoint());
    }

    @Transactional
    public RecommendModifyResponse modifyPost(String userEmail, Long recommendNo, RecommendModifyRequest request) {
        // 글을 작성하는 Member 확인
        Member member = validateGetMemberInfoByUserEmail(userEmail);

        // 수정 글 확인
        Recommend recommend = validateGetRecommendInfoByRecommendNo(recommendNo);

        // Member와 Recommen의 작성자가 동일한지 확인
        if (!Objects.equals(member.getMemberNo(), recommend.getMember().getMemberNo())) {
            throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
        }

        // 수정 게시물
        recommendRepository.update(request.getRecommendTitle(), request.getRecommendContent(), request.getRecommendImageUrl(), request.getRecommendYoutubeUrl(), recommendNo);
        recommend = validateGetRecommendInfoByRecommendNo(recommendNo);

        return new RecommendModifyResponse(recommend.getRecommendNo(), recommend.getRecommendTitle());
    }


    @Transactional
    public void deletePost(String userEmail, Long recommendNo) {
        // 글을 작성하는 Member 확인
        Member member = validateGetMemberInfoByUserEmail(userEmail);

        // 수정 글 확인
        Recommend recommend = validateGetRecommendInfoByRecommendNo(recommendNo);

        // Member와 Recommen의 작성자가 동일한지 확인
        if (!Objects.equals(member.getMemberNo(), recommend.getMember().getMemberNo())) {
            throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
        }

        recommendRepository.delete(recommend);
    }

    private Recommend validateGetRecommendInfoByRecommendNo(Long recommendNo) {
        return recommendRepository.findById(recommendNo)
                .orElseThrow(() -> {
                    throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
                });
    }

    private Member validateGetMemberInfoByUserEmail(String userEmail) {
        return memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> {
                    throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
                });
    }
}
