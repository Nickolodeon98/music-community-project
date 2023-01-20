package com.content_i_like.service;

import com.content_i_like.domain.dto.comment.CommentModifyRequest;
import com.content_i_like.domain.dto.comment.CommentRequest;
import com.content_i_like.domain.dto.comment.CommentResponse;
import com.content_i_like.domain.entity.Comment;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Recommend;
import com.content_i_like.domain.enums.MemberStatusEnum;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.CommentRepository;
import com.content_i_like.repository.MemberRepository;
import com.content_i_like.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final RecommendRepository recommendRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CommentResponse writeComment(String userEmail, CommentRequest request, Long recommendNo) {
        // 댓글 작성자를 불러옵니다
        Member member = validateGetMemberInfoByUserEmail(userEmail);

        // 댓글을 작성할 글을 불러옵니다.
        Recommend post = validateGetRecommendInfoByRecommendNo(recommendNo);

        // 댓글을 저장합니다.
        Comment comment = commentRepository.save(request.toEntity(member, post));

        return new CommentResponse(comment.getCommentNo(), post.getRecommendNo(), comment.getCommentContent(), comment.getCommentPoint());
    }

    @Transactional
    public CommentResponse modifyComment(String userEmail, CommentModifyRequest request, Long recommendNo, Long commentNo) {
        // 댓글 수정하려는 작성자
        Member member = validateGetMemberInfoByUserEmail(userEmail);

        // 댓글이 달려 있는 글을 찾습니다.
        Recommend post = validateGetRecommendInfoByRecommendNo(recommendNo);

        // 수정하려는 댓글을 찾습니다.
        Comment comment = validateGetCommentInfoByCommentNo(commentNo);

        // 댓글이 달려 있는 글의 위치가 일치하는지 확인합니다.
        if (!Objects.equals(post.getRecommendNo(), comment.getRecommend().getRecommendNo())) {
            throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
        }

        // 댓글을 수정하려는 유저와 댓글을 작성한 유저가 같은 사람인지 확인합니다.
        if (!Objects.equals(member.getMemberNo(), comment.getMember().getMemberNo())) {
            throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
        }

        // 댓글을 수정합니다.
        commentRepository.update(request.getCommentContent(), commentNo);
        comment = validateGetCommentInfoByCommentNo(commentNo);

        return new CommentResponse(comment.getCommentNo(), post.getRecommendNo(), comment.getCommentContent(), comment.getCommentPoint());
    }

    public void deleteComment(String userEmail, Long recommendNo, Long commentNo) {
        // 댓글 삭제하려는 작성자
        Member member = validateGetMemberInfoByUserEmail(userEmail);

        // 댓글이 달려 있는 글을 찾습니다.
        Recommend post = validateGetRecommendInfoByRecommendNo(recommendNo);

        // 삭제하려는 댓글을 찾습니다.
        Comment comment = validateGetCommentInfoByCommentNo(commentNo);

        // 댓글이 달려 있는 글의 위치가 일치하는지 확인합니다.
        if (!Objects.equals(post.getRecommendNo(), comment.getRecommend().getRecommendNo())) {
            throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
        }

        // 댓글을 삭제하려는 유저가 댓글을 작성한 유저이거나, 게시물 작성 유저이거나, 관리자일 경우 삭제가 가능합니다.
        if (!Objects.equals(member.getMemberNo(), comment.getMember().getMemberNo())
                && !Objects.equals(member.getMemberNo(), post.getMember().getMemberNo())
                && !member.getStatus().equals(MemberStatusEnum.ADMIN)) {
            throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
        }

        // 댓글을 삭제합니다.
        commentRepository.delete(comment);
    }

    private Comment validateGetCommentInfoByCommentNo(Long commentNo) {
        return commentRepository.findById(commentNo)
                .orElseThrow(() -> {
                    throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
                });
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
