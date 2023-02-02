package com.content_i_like.service.searchtools;

import com.content_i_like.domain.dto.search.SearchMembersResponse;
import com.content_i_like.repository.MemberRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class MembersSearchTool implements ItemSearch<SearchMembersResponse> {

  private final MemberRepository memberRepository;

  @Override
  public Page<SearchMembersResponse> searchAll(Pageable pageable) {
    return memberRepository.findAll(pageable)
        .map(SearchMembersResponse::of);
  }

  @Override
  public Page<SearchMembersResponse> search(String keyword, Pageable pageable) {
    return memberRepository.findByNickNameContaining(keyword, pageable)
        .map(memberPage -> memberPage.map(SearchMembersResponse::of))
        .orElseGet(() -> new PageImpl<>(Collections.emptyList()));
  }
}
