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

  public AnswerResponse getAnswer(Long inquiryNo) {
    Inquiry inquiry = inquiryRepository.findById(inquiryNo)
        .orElseThrow(() -> new ContentILikeAppException(ErrorCode.NOT_FOUND,
            String.format("inquiryNo: %d not found", inquiryNo)));

    return AnswerResponse.of(answerRepository.findByInquiry(inquiry), inquiry.getCreatedAt());
  }

  public AnswerResponse postAnswer(Long inquiryNo, AnswerRequire answerRequire) {
    Inquiry inquiry = inquiryRepository.findById(inquiryNo)
        .orElseThrow(() -> new ContentILikeAppException(ErrorCode.NOT_FOUND,
            String.format("inquiryNo: %d not found", inquiryNo)));

    Answer answer = Answer.builder()
        .answerContent(answerRequire.getContent())
        .inquiry(inquiry)
        .build();

    return AnswerResponse.of(answerRepository.save(answer), inquiry.getCreatedAt());
  }
}
