package com.content_i_like.service;

import com.content_i_like.domain.dto.comment.CommentModifyRequest;
import com.content_i_like.domain.dto.comment.CommentReadResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final RecommendRepository recommendRepository;
  private final MemberRepository memberRepository;

  /**
   * 추천글에 댓글을 작성합니다.
   *
   * @param userEmail   댓글을 작성하는 사용자 email
   * @param request     작성하는 댓글 정보
   * @param recommendNo 댓글을 작성할 추천글의 고유 번호
   * @return 작성된 댓글 내용
   */
  @Transactional
  public CommentResponse writeComment(final String userEmail, final CommentRequest request,
      final Long recommendNo) {
    // 댓글 작성자를 불러옵니다
    Member member = validateGetMemberInfoByUserEmail(userEmail);

    // 댓글을 작성할 글을 불러옵니다.
    Recommend post = validateGetRecommendInfoByRecommendNo(recommendNo);

    // 댓글을 저장합니다.
    Comment comment = commentRepository.save(request.toEntity(member, post));

    return new CommentResponse(comment.getCommentNo(), post.getRecommendNo(),
        comment.getCommentContent(), comment.getCommentPoint());
  }

  /**
   * 등록된 댓글을 수정합니다.
   *
   * @param userEmail   댓글 수정을 요청하는 사용자 email
   * @param request     수정할 댓글 정보
   * @param recommendNo 수정할 대슥ㄹ이 존재하는 추천글 고유번호
   * @param commentNo   수정할 댓글 고유번호
   * @return 수정된 댓글 내용
   */
  @Transactional
  public CommentResponse modifyComment(final String userEmail, final CommentModifyRequest request,
      final Long recommendNo, final Long commentNo) {
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

    return new CommentResponse(comment.getCommentNo(), post.getRecommendNo(),
        comment.getCommentContent(), comment.getCommentPoint());
  }

  /**
   * 작성된 댓글을 삭제합니다.
   *
   * @param userEmail   삭제 요청을 보낸 사용자 email
   * @param recommendNo 삭제할 댓글이 존재하는 추천글 고유번호
   * @param commentNo   삭제할 댓글의 고유번호
   */
  public void deleteComment(final String userEmail, final Long recommendNo, final Long commentNo) {
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

  /**
   * 추천글에 작성된 특정 댓글 정보를 불러옵니다.
   *
   * @param recommendNo 가져올 댓글이 존재하는 추천글 고유번호
   * @param commentNo   정보를 반환할 댓글 고유번호
   * @return 댓글 정보
   */
  @Transactional
  public CommentReadResponse getReadComment(final Long recommendNo, final Long commentNo) {
    // 해댕 추천글의 NO와 댓글 NO가 일치하는 댓글을 찾아옵니다.
    Comment comment = commentRepository.findCommentByRecommend_RecommendNoAndCommentNo(recommendNo,
            commentNo)
        .orElseThrow(() -> {
          throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
        });

    return CommentReadResponse.of(comment);
  }

  /**
   * 추천글에 작성된 모든 댓글 정보를 불러옵ㄴ디ㅏ.
   *
   * @param pageable    페이징 정보
   * @param recommendNo 댓글을 가져올 추천글 고유번호
   * @return 해당 추천글의 모든 댓글 정보
   */
  @Transactional
  public Page<CommentReadResponse> getReadAllComment(final Pageable pageable,
      final Long recommendNo) {
    return commentRepository.findAllByRecommendRecommendNo(recommendNo, pageable)
        .map(CommentReadResponse::of);
  }

  private Comment validateGetCommentInfoByCommentNo(final Long commentNo) {
    return commentRepository.findById(commentNo)
        .orElseThrow(() -> {
          throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
        });
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

