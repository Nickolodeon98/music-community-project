package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.answer.AnswerRequire;
import com.content_i_like.domain.dto.answer.AnswerResponse;
import com.content_i_like.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {

  private final AnswerService answerService;


  /**
   * 1:1문의 답변을 반환합니다
   *
   * @param inquiryNo   1:1문의 번호
   * @return 답변 번호, 답변 날짜, 답변 내용
   */
  @GetMapping("/{inquiryNo}")
  public Response<AnswerResponse> getAnswer(@PathVariable Long inquiryNo) {
    return Response.success(answerService.getAnswer(inquiryNo));
  }

  /**
   * 1:1문의 답변을 등록합니다
   *
   * @param inquiryNo   1:1문의 번호
   * @param answerRequire 1:1문의 답변 내용
   * @return 답변 번호, 답변 날짜, 답변 내용
   */
  @PostMapping("/{inquiryNo}")
  public Response<AnswerResponse> postAnswer(@PathVariable Long inquiryNo,
      @RequestBody AnswerRequire answerRequire) {
    return Response.success(answerService.postAnswer(inquiryNo, answerRequire));
  }
}
