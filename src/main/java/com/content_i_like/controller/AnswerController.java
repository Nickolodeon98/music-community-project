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
@RequestMapping("/api/v1/answer")
public class AnswerController {

  private final AnswerService answerService;

  @GetMapping("/{inquiryNo}")
  public Response<AnswerResponse> getAnswer(@PathVariable Long inquiryNo) {
    return Response.success(answerService.getAnswer(inquiryNo));
  }

  @PostMapping("/{inquiryNo}")
  public Response<AnswerResponse> postAnswer(@PathVariable Long inquiryNo,
      @RequestBody AnswerRequire answerRequire) {
    return Response.success(answerService.postAnswer(inquiryNo, answerRequire));
  }
}
