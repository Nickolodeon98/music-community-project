package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.faq.FaqDetailsResponse;
import com.content_i_like.domain.dto.faq.FaqRequest;
import com.content_i_like.domain.dto.faq.FaqResponse;
import com.content_i_like.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class FaqController {

  private final FaqService faqService;

  @RequestMapping("faq/test")
  public String test() {
    return "test";
  }

  @RequestMapping("testFaq")
  public String test1() {
    return "test";
  }


  @GetMapping("faq")
  public String getAllFaq(Pageable pageable) {
    Page<FaqResponse> allFaq = faqService.getAllFaq(pageable);

    return null;
  }

//  @GetMapping("/{category}")
//  public Response<Page<FaqResponse>> getFaqByCategory(Pageable pageable,
//      @PathVariable String category, Model model) {
//    return null;
//  }
//
//  @GetMapping("/searches/{keyWord}")
//  public Response<Page<FaqResponse>> getFaqByKeyWord(Pageable pageable,
//      @PathVariable String keyWord) {
//    return Response.success(faqService.getFaqByKeyWord(pageable, keyWord));
//  }
//
//  @GetMapping("/details/{faqNo}")
//  public Response<FaqDetailsResponse> getFaqDetails(@PathVariable Long faqNo) {
//    return Response.success(faqService.getFaqDetails(faqNo));
//  }
//
//  @PostMapping()
//  public Response<FaqResponse> addFaq(@RequestBody FaqRequest faqRequest) {
//    return Response.success(faqService.addFaq(faqRequest));
//  }
}
