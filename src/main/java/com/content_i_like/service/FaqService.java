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

    public Page<FaqResponse> getAllFaq(Pageable pageable){
        return faqRepository.findAll(pageable).map(FaqResponse::of);
    }

    public Page<FaqResponse> getFaqByCategory(Pageable pageable, String faqCategory) {
        return faqRepository.findAllByFaqCategory(pageable, faqCategory).map(FaqResponse::of);
    }

    public Page<FaqResponse> getFaqByKeyWord(Pageable pageable, String keyWord) {
        return faqRepository.findAllByFaqTitleContaining(pageable, keyWord).map(FaqResponse::of);
    }

    public FaqDetailsResponse getFaqDetails(Long faqNo) {

        //faq가 존재하는지 검증
        Optional<FAQ> optionalFAQ = faqRepository.findById(faqNo);
        optionalFAQ.orElseThrow(() -> new ContentILikeAppException(ErrorCode.NOT_FOUND, String.format("faqNo:%d not exist")));

        return FaqDetailsResponse.of(optionalFAQ.get());
    }

    public FaqResponse addFaq(FaqRequest faqRequest) {
        return FaqResponse.of(faqRepository.save(faqRequest.toEntity()));
    }
}
