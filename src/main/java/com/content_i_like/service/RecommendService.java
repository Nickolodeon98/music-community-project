package com.content_i_like.service;

import com.content_i_like.domain.dto.recommend.*;
import com.content_i_like.domain.entity.*;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final MemberRepository memberRepository;
    private final RecommendRepository recommendRepository;

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final SongRepository songRepository;


    @Transactional
    public RecommendPostResponse uploadPost(final String userEmail, final RecommendPostRequest request) {
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
    public RecommendModifyResponse modifyPost(final String userEmail, final Long recommendNo, final RecommendModifyRequest request) {
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
    public void deletePost(final String userEmail, final Long recommendNo) {
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

    public RecommendReadResponse readPost(final Long recommendNo) {
        // 해당 글을 불러 옵니다.
        Recommend post = validateGetRecommendInfoByRecommendNo(recommendNo);

        // 해당 글의 작성자 정보를 받아옵니다.
        Member member = validateGetMemberInfoByUserEmail(post.getMember().getEmail());

        // 해당 글의 음악 정보를 받아옵니다.
        Song song = songRepository.findById(post.getSong().getSongNo())
                .orElseThrow(() -> {
                    throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
                });

        // 앨범 정보를 받아옵니다.
        Album album = albumRepository.findById(song.getAlbum().getAlbumNo())
                .orElseThrow(() -> {
                    throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
                });

        // 아티스트의 정보를 받아옵니다.
        Artist artist = artistRepository.findById(album.getArtist().getArtistNo())
                .orElseThrow(() -> {
                    throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
                });

        // 해당 글에 속해있는 comment 목록
        List<Comment> comments = post.getComments();

        // 좋아요 총 합
        Long countLikes = (long) post.getLikes().size();

        // 댓글과 게시물의 포인트 총 합
        Long accumulatedPoints = comments.stream()
                .mapToLong(Comment::getCommentPoint)
                .sum();


        return RecommendReadResponse.builder()
                .recommendTitle(post.getRecommendTitle())
                .memberNickname(member.getNickName())
                .albumImageUrl(album.getAlbumImageUrl())
                .songTitle(song.getSongTitle())
                .artistName(artist.getArtistName())
                .comments(comments)
                .recommendContent(post.getRecommendContent())
                .countLikes(countLikes)
                .recommendPoint(post.getRecommendPoint())
                .accumulatedPoints(accumulatedPoints)
                .recommendYoutubeUrl(post.getRecommendYoutubeUrl())
                .build();
    }

    private Recommend validateGetRecommendInfoByRecommendNo(final Long recommendNo) {
        return recommendRepository.findById(recommendNo)
                .orElseThrow(() -> {
                    throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
                });
    }

    private Member validateGetMemberInfoByUserEmail(final String userEmail) {
        return memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> {
                    throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
                });
    }
}
