package com.content_i_like.service;

import com.content_i_like.domain.dto.faq.FaqDetailsResponse;
import com.content_i_like.domain.dto.faq.FaqRequest;
import com.content_i_like.domain.dto.faq.FaqResponse;
import com.content_i_like.domain.entity.FAQ;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.FAQRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FaqService {

  private final FAQRepository faqRepository;

  /**
   * 모든 FAQ를 반환합니다.
   *
   * @return 모든 FAQ를 page로 반환
   */
  public Page<FaqResponse> getAllFaq(Pageable pageable) {
    return faqRepository.findAll(pageable).map(FaqResponse::of);
  }

  /**
   * 특정 카테고리의 FAQ를 반환합니다
   *
   * @param faqCategory  반환하고자 하는 카테고리의 종류
   * @return 해당 param 카테고리의 FAQ를 반환
   */
  public Page<FaqResponse> getFaqByCategory(Pageable pageable, final String faqCategory) {
    return faqRepository.findAllByFaqCategory(pageable, faqCategory).map(FaqResponse::of);
  }

  /**
   * 특정 키워드를 포함한 FAQ를 반환합니다
   *
   * @param keyWord  반환하고자 하는 FAQ가 포함한 키워드
   * @return 해당 키워드를 포함한 FAQ
   */
  public Page<FaqResponse> getFaqByKeyWord(Pageable pageable, final String keyWord) {
    return faqRepository.findAllByFaqTitleContaining(pageable, keyWord).map(FaqResponse::of);
  }

  /**
   * 특정 FAQ의 내용을 반환합니다
   *
   * @param faqNo  반환하고자 FAQ의 번호
   * @return faqNo의 FAQ를 반환
   */
  public FaqDetailsResponse getFaqDetails(final Long faqNo) {

    //faq가 존재하는지 검증
    Optional<FAQ> optionalFAQ = faqRepository.findById(faqNo);
    optionalFAQ.orElseThrow(() -> new ContentILikeAppException(ErrorCode.NOT_FOUND,
        String.format("faqNo:%d not exist", faqNo)));

    return FaqDetailsResponse.of(optionalFAQ.get());
  }

  /**
   * FAQ를 등록합니다
   *
   * @return 등록한 FAQ를 반환
   */
  public FaqResponse addFaq(FaqRequest faqRequest) {
    return FaqResponse.of(faqRepository.save(faqRequest.toEntity()));
  }
}
