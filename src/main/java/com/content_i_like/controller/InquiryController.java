
package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.answer.AnswerResponse;
import com.content_i_like.domain.dto.faq.FaqCategoryResponse;
import com.content_i_like.domain.dto.inquiry.InquiryDetailResponse;
import com.content_i_like.domain.dto.inquiry.InquiryRequire;
import com.content_i_like.domain.dto.inquiry.InquiryResponse;
import com.content_i_like.domain.dto.member.MemberLoginRequest;
import com.content_i_like.domain.dto.member.MemberLoginResponse;
import com.content_i_like.service.InquiryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inquiry")
@RequiredArgsConstructor
@Slf4j
public class InquiryController {

  private final InquiryService inquiryService;

  @PostMapping("/post")
  public String postInquiry(
      @Valid @ModelAttribute("inquiryRequire") InquiryRequire inquiryRequire,
      BindingResult bindingResult,
      HttpServletRequest request) {

    if (bindingResult.hasErrors()) {
      return "pages/member/login";
    }

    HttpSession session = request.getSession(false);
    MemberLoginResponse loginResponse = (MemberLoginResponse) session.getAttribute("loginUser");
    inquiryService.postInquiryByNickName(loginResponse.getNickName(), inquiryRequire);

    return "redirect:/faq";
  }

  @GetMapping("/writeForm")
  public String registerInquiry(Model model) {

    model.addAttribute("inquiryRequire", new InquiryRequire());
    return "pages/faq/faq-register-inquiry";
  }

  @GetMapping()
  public String getInquiryList(HttpServletRequest request, Model model, Pageable pageable) {

    HttpSession session = request.getSession(false);
    MemberLoginResponse loginResponse = (MemberLoginResponse) session.getAttribute("loginUser");
    Page<InquiryResponse> inquiryResponses = inquiryService.getInquiryListByNickName(pageable,
        loginResponse.getNickName());

    if (inquiryResponses.isEmpty()) {
      return "pages/faq/Inquire-empty";
    }

    model.addAttribute("inquiryList", inquiryResponses);
    return "pages/faq/Inquire";
  }

  @GetMapping("/answer/{inquiryNo}")
  public String getInquiryAnswer(@PathVariable Long inquiryNo, Model model) {

    InquiryDetailResponse inquiryDetails = inquiryService.getInquiryDetails(inquiryNo);
    AnswerResponse inquiryAnswer = inquiryService.getInquiryAnswer(inquiryNo);

    model.addAttribute("inquiryDetail", inquiryDetails);
    model.addAttribute("inquiryAnswer", inquiryAnswer);
    return "pages/faq/Inquire-answer";
  }

  @GetMapping("/{inquiryNo}")
  public String getInquiryDetails(@PathVariable Long inquiryNo, Model model) {
    InquiryDetailResponse inquiryDetails = inquiryService.getInquiryDetails(inquiryNo);

    model.addAttribute("inquiryDetail", inquiryDetails);
    return "pages/faq/Inquire-details";
  }
}
