package com.content_i_like.service;

import com.content_i_like.domain.dto.faq.FaqResponse;
import com.content_i_like.domain.entity.FAQ;
import com.content_i_like.repository.FAQRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class FaqService {

    private final FAQRepository faqRepository;

    public Page<FaqResponse> getAllFaq(Pageable pageable){
        return faqRepository.findAll(pageable).map(FaqResponse::of);
    }

}
