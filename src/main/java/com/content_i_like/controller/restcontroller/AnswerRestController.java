package com.content_i_like.controller.restcontroller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.answer.AnswerRequire;
import com.content_i_like.domain.dto.answer.AnswerResponse;
import com.content_i_like.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/answer")
public class AnswerRestController {

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
