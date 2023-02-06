
package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.inquiry.InquiryRequire;
import com.content_i_like.domain.dto.inquiry.InquiryResponse;
import com.content_i_like.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inquiry")
@RequiredArgsConstructor
public class InquiryController {

  private final InquiryService inquiryService;

  @PostMapping()
  public Response<InquiryResponse> postInquiry(@RequestBody InquiryRequire inquiryRequire,
      Authentication authentication) {
    return Response.success(inquiryService.postInquiry(authentication.getName(), inquiryRequire));
  }

  @GetMapping()
  public Response<Page<InquiryResponse>> getInquiryList(Pageable pageable,
      Authentication authentication) {
    return Response.success(inquiryService.getInquiryList(pageable, authentication.getName()));
  }
}
