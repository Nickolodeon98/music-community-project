package com.content_i_like.service;

import com.content_i_like.domain.dto.comment.CommentRequest;
import com.content_i_like.domain.dto.comment.CommentResponse;
import com.content_i_like.domain.entity.Comment;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Recommend;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.CommentRepository;
import com.content_i_like.repository.MemberRepository;
import com.content_i_like.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final RecommendRepository recommendRepository;
    private final MemberRepository memberRepository;

    public CommentResponse writeComment(String userEmail, CommentRequest request, Long recommendNo) {
        // 댓글 작성자를 불러옵니다
        Member member = validateGetMemberInfoByUserEmail(userEmail);

        // 댓글을 작성할 글을 불러옵니다.
        Recommend post = validateGetRecommendInfoByRecommendNo(recommendNo);

        // 댓글을 저장합니다.
        Comment comment = commentRepository.save(request.toEntity(member, post));

        return new CommentResponse(comment.getCommentNo(), post.getRecommendNo(), comment.getCommentContent(), comment.getCommentPoint());
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
