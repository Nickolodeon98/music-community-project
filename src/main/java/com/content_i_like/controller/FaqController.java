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

  /**
   * 모든 FAQ를 반환합니다.
   *
   * @return 모든 FAQ를 page로 반환
   */
  @GetMapping()
  public String getAllFaq(Pageable pageable, Model model) {
    Page<FaqResponse> allFaq = faqService.getAllFaq(pageable);

    model.addAttribute("faq", allFaq);
    return "pages/faq/faq";
  }

  /**
   * 특정 카테고리의 FAQ를 반환합니다
   *
   * @param category  반환하고자 하는 카테고리의 종류
   * @return 해당 param 카테고리의 FAQ를 반환
   */
  @GetMapping("/{category}")
  public String getFaqByCategory(Pageable pageable,
      @PathVariable String category, Model model) {
    Page<FaqResponse> faqByCategory = faqService.getFaqByCategory(pageable, category);

    model.addAttribute("categoryInfo", category);
    model.addAttribute("faq", faqByCategory);
    return "pages/faq/faq";
  }

  /**
   * 특정 키워드를 포함한 FAQ를 반환합니다
   *
   * @param keyWord  반환하고자 하는 FAQ가 포함한 키워드
   * @return 해당 키워드를 포함한 FAQ
   */
  @GetMapping("/searches/{keyWord}")
  public String getFaqByKeyWord(Pageable pageable,
      @PathVariable String keyWord, Model model) {
    Page<FaqResponse> faqByKeyWord = faqService.getFaqByKeyWord(pageable, keyWord);

    model.addAttribute("faq", faqByKeyWord);
    return "pages/faq/faq";
  }

  /**
   * 특정 FAQ의 내용을 반환합니다
   *
   * @param faqNo  반환하고자 FAQ의 번호
   * @return faqNo의 FAQ를 반환
   */
  @GetMapping("/details/{faqNo}")
  public String getFaqDetails(@PathVariable Long faqNo, Model model) {
    FaqDetailsResponse faqDetails = faqService.getFaqDetails(faqNo);

    model.addAttribute("faqDetails", faqDetails);
    return "pages/faq/faq-details";
  }
}
