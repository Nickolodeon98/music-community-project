package com.content_i_like.service;

import com.content_i_like.domain.dto.answer.AnswerRequire;
import com.content_i_like.domain.dto.answer.AnswerResponse;
import com.content_i_like.domain.entity.Answer;
import com.content_i_like.domain.entity.Inquiry;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.AnswerRepository;
import com.content_i_like.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {

  private final AnswerRepository answerRepository;

  private final InquiryRepository inquiryRepository;

  /**
   * 1:1문의 답변을 반환합니다
   *
   * @param inquiryNo   1:1문의 번호
   * @return 답변 번호, 답변 날짜, 답변 내용
   */
  public AnswerResponse getAnswer(final Long inquiryNo) {
    Inquiry inquiry = inquiryRepository.findById(inquiryNo)
        .orElseThrow(() -> new ContentILikeAppException(ErrorCode.NOT_FOUND,
            String.format("inquiryNo: %d not found", inquiryNo)));

    return AnswerResponse.of(answerRepository.findByInquiry(inquiry));
  }

  /**
   * 1:1문의 답변을 등록합니다
   *
   * @param inquiryNo   1:1문의 번호
   * @param answerRequire 1:1문의 답변 내용
   * @return 답변 번호, 답변 날짜, 답변 내용
   */
  public AnswerResponse postAnswer(final Long inquiryNo, AnswerRequire answerRequire) {
    Inquiry inquiry = inquiryRepository.findById(inquiryNo)
        .orElseThrow(() -> new ContentILikeAppException(ErrorCode.NOT_FOUND,
            String.format("inquiryNo: %d not found", inquiryNo)));

    Answer answer = Answer.builder()
        .answerContent(answerRequire.getContent())
        .inquiry(inquiry)
        .build();

    return AnswerResponse.of(answerRepository.save(answer));
  }
}
