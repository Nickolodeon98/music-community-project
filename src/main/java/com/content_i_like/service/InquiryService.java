package com.content_i_like.service;

import com.content_i_like.domain.dto.answer.AnswerResponse;
import com.content_i_like.domain.dto.inquiry.InquiryDetailResponse;
import com.content_i_like.domain.dto.inquiry.InquiryRequire;
import com.content_i_like.domain.dto.inquiry.InquiryResponse;
import com.content_i_like.domain.entity.Answer;
import com.content_i_like.domain.entity.Inquiry;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.AnswerRepository;
import com.content_i_like.repository.InquiryRepository;
import com.content_i_like.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InquiryService {

  private final InquiryRepository inquiryRepository;

  private final MemberRepository memberRepository;

  private final AnswerRepository answerRepository;

  /**
   * 1:1문의를 동록합니다
   *
   * @param userEmail 1:1문의를 등록한 유저의 email
   * @param inquiryRequire 1:1문의 제목과 내용
   * @return 등록한 1:1문의의 상세내용
   */
  public InquiryResponse postInquiry(final String userEmail, InquiryRequire inquiryRequire) {

    Member member = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new ContentILikeAppException(ErrorCode.NOT_FOUND,
            String.format("Email: %s not found", userEmail)));

    Inquiry inquiry = Inquiry.builder()
        .inquiryTitle(inquiryRequire.getTitle())
        .inquiryContent(inquiryRequire.getContent())
        .member(member)
        .build();

    Inquiry savedInquiry = inquiryRepository.save(inquiry);

    return InquiryResponse.of(savedInquiry);
  }

  /**
   * 1:1문의 목록을 반환합니다
   *
   * @param userEmail 반환하고자 하는 1:1문의 목록의 유저
   * @return 1:1문의 목록
   */
  public Page<InquiryResponse> getInquiryList(Pageable pageable, final String userEmail) {

    Member member = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new ContentILikeAppException(ErrorCode.NOT_FOUND,
            String.format("Email: %s not found", userEmail)));

    Page<Inquiry> inquiries = inquiryRepository.findAllByMemberAndCreatedAtAfter(pageable, member,
        LocalDateTime.now().minusMonths(6));
    return inquiries.map(InquiryResponse::of);
  }

  /**
   * 1:1 문의를 등록합니다
   *
   * @param nickName 1:1문의를 등록한 유저의 닉네임
   * @return 등록한 1:1 문의의 상세 내용
   */
  public InquiryResponse postInquiryByNickName(final String nickName,
      InquiryRequire inquiryRequire) {

    Member member = memberRepository.findByNickName(nickName)
        .orElseThrow(() -> new ContentILikeAppException(ErrorCode.MEMBER_NOT_FOUND,
            ErrorCode.MEMBER_NOT_FOUND.getMessage()));

    if (inquiryRequire.getTitle().isEmpty()) {
      throw new ContentILikeAppException(ErrorCode.NOT_EXIST_INQUIRY_TITLE,
          ErrorCode.NOT_EXIST_INQUIRY_TITLE.getMessage());
    }

    if (inquiryRequire.getContent().isEmpty()) {
      throw new ContentILikeAppException(ErrorCode.NOT_EXIST_INQUIRY_CONTENT,
          ErrorCode.NOT_EXIST_INQUIRY_CONTENT.getMessage());
    }

    Inquiry inquiry = Inquiry.builder()
        .inquiryTitle(inquiryRequire.getTitle())
        .inquiryContent(inquiryRequire.getContent())
        .processingStatus("처리중")
        .member(member)
        .build();

    Inquiry savedInquiry = inquiryRepository.save(inquiry);

    return InquiryResponse.of(savedInquiry);
  }

  /**
   * 해당 닉네임의 1:1문의 목록을 반환합니다
   *
   * @param nickName 찾고자 하는 목록의 닉네임
   * @return 1:1문의 목록
   */
  public Page<InquiryResponse> getInquiryListByNickName(Pageable pageable, final String nickName) {

    Member member = memberRepository.findByNickName(nickName)
        .orElseThrow(() -> new ContentILikeAppException(ErrorCode.MEMBER_NOT_FOUND,
            ErrorCode.MEMBER_NOT_FOUND.getMessage()));

    Page<Inquiry> inquiries = inquiryRepository.findAllByMemberAndCreatedAtAfter(pageable, member,
        LocalDateTime.now().minusMonths(6));
    return inquiries.map(InquiryResponse::of);
  }

  /**
   * 1:1문의의 상세 내용을 반환합니다.
   *
   * @param inquiryNo 확인하고자 하는 문의의 번호
   * @return 1:1문의의 상세 내용
   */
  public InquiryDetailResponse getInquiryDetails(Long inquiryNo) {
    Inquiry inquiry = inquiryRepository.findById(inquiryNo).orElseThrow(
        () -> new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage()));

    return InquiryDetailResponse.of(inquiry);
  }

  /**
   * 1:1문의에 등록된 답변을 반환합니다
   *
   * @param inquiryNo 확인하고자 하는 문의의 번호
   * @return 1:1문의에 등록된 답변
   */
  public AnswerResponse getInquiryAnswer(Long inquiryNo) {
    Inquiry inquiry = inquiryRepository.findById(inquiryNo).orElseThrow(
        () -> new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage()));
    Answer byInquiry = answerRepository.findByInquiry(inquiry);
    return AnswerResponse.of(byInquiry);
  }
}
