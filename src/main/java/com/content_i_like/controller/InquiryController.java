
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

  /**
   * 1:1문의를 동록합니다
   *
   * @param inquiryRequire 1:1문의 제목과 내용
   * @return 로그인 되있지 않다면 로그인 페이지
   *         로그인 되어 있다면 faq 페이지
   */
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

  /**
   * 1:1문의 등록 페이지를 반환합니다
   *
   * @return 1:1문의 등록 페이지
   */
  @GetMapping("/writeForm")
  public String registerInquiry(Model model) {

    model.addAttribute("inquiryRequire", new InquiryRequire());
    return "pages/faq/faq-register-inquiry";
  }

  /**
   * 유저가 6개월내에 등록한 1:1문의 내역을 반환합니다
   *
   * @return 유저가 6개월내에 등록한 1:1문의 내역
   */
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

  /**
   * 1:1문의에 등록된 답변을 반환합니다
   *
   * @param inquiryNo 확인하고자 하는 문의의 번호
   * @return 1:1문의에 등록된 답변
   */
  @GetMapping("/answer/{inquiryNo}")
  public String getInquiryAnswer(@PathVariable Long inquiryNo, Model model) {

    InquiryDetailResponse inquiryDetails = inquiryService.getInquiryDetails(inquiryNo);
    AnswerResponse inquiryAnswer = inquiryService.getInquiryAnswer(inquiryNo);

    model.addAttribute("inquiryDetail", inquiryDetails);
    model.addAttribute("inquiryAnswer", inquiryAnswer);
    return "pages/faq/Inquire-answer";
  }

  /**
   * 1:1문의의 상세 내용을 반환합니다.
   *
   * @param inquiryNo 확인하고자 하는 문의의 번호
   * @return 1:1문의의 상세 내용
   */
  @GetMapping("/{inquiryNo}")
  public String getInquiryDetails(@PathVariable Long inquiryNo, Model model) {
    InquiryDetailResponse inquiryDetails = inquiryService.getInquiryDetails(inquiryNo);

    model.addAttribute("inquiryDetail", inquiryDetails);
    return "pages/faq/Inquire-details";
  }
}
