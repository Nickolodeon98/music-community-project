package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.faq.FaqCategoryResponse;
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
@RequestMapping("/faq")
public class FaqController {

  private final FaqService faqService;

  @GetMapping()
  public String getAllFaq(Pageable pageable, Model model) {
    Page<FaqResponse> allFaq = faqService.getAllFaq(pageable);

    model.addAttribute("faq", allFaq);
    return "pages/faq/faq";
  }

  @GetMapping("/{category}")
  public String getFaqByCategory(Pageable pageable,
      @PathVariable String category, Model model) {
    Page<FaqResponse> faqByCategory = faqService.getFaqByCategory(pageable, category);

    model.addAttribute("faq", faqByCategory);
    return "pages/faq/faq";
  }

  @GetMapping("/searches/{keyWord}")
  public String getFaqByKeyWord(Pageable pageable,
      @PathVariable String keyWord, Model model) {
    Page<FaqResponse> faqByKeyWord = faqService.getFaqByKeyWord(pageable, keyWord);

    model.addAttribute("faq", faqByKeyWord);
    return "pages/faq/faq";
  }

  @GetMapping("/details/{faqNo}")
  public String getFaqDetails(@PathVariable Long faqNo, Model model) {
    FaqDetailsResponse faqDetails = faqService.getFaqDetails(faqNo);

    model.addAttribute("faqDetails", faqDetails);
    return "pages/faq/faq-details";
  }
//
//  @PostMapping()
//  public Response<FaqResponse> addFaq(@RequestBody FaqRequest faqRequest) {
//    return Response.success(faqService.addFaq(faqRequest));
//  }
}
