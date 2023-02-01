package com.content_i_like.domain.dto.search;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@NoArgsConstructor
public class SearchPageGetResponse<T> {
    private String message;
    private Page<T> tracks;

    public SearchPageGetResponse(String message, Page<T> tracks) {
        this.message = message;
        this.tracks = tracks;
    }
}
