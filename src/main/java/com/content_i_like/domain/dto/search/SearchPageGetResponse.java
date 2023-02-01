package com.content_i_like.domain.dto.search;

import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchPageGetResponse<T> {
    private String message;
    private Page<T> pages;


    public static <T> SearchPageGetResponse<T> of(String message, Page<T> pages) {
        return new SearchPageGetResponse<>(message, pages);
    }
}
