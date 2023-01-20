package com.content_i_like.service;

import com.content_i_like.domain.dto.inquiry.InquiryRequire;
import com.content_i_like.domain.dto.inquiry.InquiryResponse;
import com.content_i_like.domain.entity.Inquiry;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.InquiryRepository;
import com.content_i_like.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    private final MemberRepository memberRepository;

    //1:1 문의 등록하기
    public InquiryResponse postInquiry(String userEmail, InquiryRequire inquiryRequire) {

        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ContentILikeAppException(ErrorCode.NOT_FOUND, String.format("Email: %s not found", userEmail)));

        Inquiry inquiry = Inquiry.builder()
                .inquiryTitle(inquiryRequire.getTitle())
                .inquiryContent(inquiryRequire.getContent())
                .member(member)
                .build();

        Inquiry savedInquiry = inquiryRepository.save(inquiry);

        return InquiryResponse.of(savedInquiry, "처리중");
    }
}
