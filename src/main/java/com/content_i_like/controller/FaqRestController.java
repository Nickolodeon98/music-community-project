package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.faq.FaqResponse;
import com.content_i_like.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/faq")
public class FaqRestController {

    private final FaqService faqService;

    @GetMapping()
    public Response<Page<FaqResponse>> getAllFaq(@PageableDefault(size = 20) Pageable pageable){
        return Response.success(faqService.getAllFaq(pageable));
    }

}
