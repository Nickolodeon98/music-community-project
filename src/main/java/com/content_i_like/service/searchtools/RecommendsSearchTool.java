package com.content_i_like.service.searchtools;

import com.content_i_like.domain.dto.search.SearchMembersResponse;
import com.content_i_like.domain.dto.search.SearchRecommendsResponse;
import com.content_i_like.domain.entity.Recommend;
import com.content_i_like.repository.RecommendRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class RecommendsSearchTool implements ItemSearch<SearchRecommendsResponse> {

  private final RecommendRepository recommendRepository;

  @Override
  public Page<SearchRecommendsResponse> searchAll(Pageable pageable) {
    return recommendRepository.findAll(pageable).map(recommend ->
        recommend.getRecommendContent().length() < 10
            ? SearchRecommendsResponse.of(recommend, recommend.getRecommendContent())
            : SearchRecommendsResponse.of(recommend,
                recommend.getRecommendContent().substring(0, 10)));
  }

  @Override
  public Page<SearchRecommendsResponse> search(String keyword, Pageable pageable) {

    return recommendRepository.findByRecommendTitleContaining(keyword, pageable)
        .map(recommendPage -> recommendPage
            .map(recommend ->
                recommend.getRecommendContent().length() < 100
                    ? SearchRecommendsResponse.of(recommend, recommend.getRecommendContent())
                    : SearchRecommendsResponse.of(recommend,
                        recommend.getRecommendContent().substring(0, 100))))
        .orElseGet(() -> new PageImpl<>(Collections.emptyList()));
  }
}
