package com.content_i_like.service;

import static com.content_i_like.service.validchecks.ArbitraryValidationService.validate;

import com.content_i_like.domain.dto.recommend.RecommendReadResponse;
import com.content_i_like.domain.dto.search.SearchMembersResponse;
import com.content_i_like.domain.dto.search.SearchPageGetResponse;
import com.content_i_like.domain.dto.tracks.TrackGetResponse;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.repository.MemberRepository;
import com.content_i_like.repository.TrackRepository;
import com.content_i_like.service.searchtools.ItemSearch;
import com.content_i_like.service.searchtools.MembersSearchTool;
import com.content_i_like.service.searchtools.TracksSearchTool;
import com.content_i_like.service.validchecks.MemberValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {
  private final TrackRepository trackRepository;
  private final MemberRepository memberRepository;

  public <T> SearchPageGetResponse<T> getEveryItem(Pageable pageable, String memberEmail,
      ItemSearch<T> searchTool) {
    Member member = validate(memberEmail, new MemberValidation(memberRepository));
    Page<T> pagedItems = searchTool.searchAll(pageable);

    return SearchPageGetResponse.of(
        String.format("총 %s개의 검색결과를 찾았습니다.", pagedItems.getTotalElements()), pagedItems);
  }

  public <T> SearchPageGetResponse<T> findWithKeyword(Pageable pageable, String searchKey,
      String memberEmail, ItemSearch<T> searchTool) {
    Member member = validate(memberEmail, new MemberValidation(memberRepository));

    Page<T> pagedItems = searchTool.search(searchKey, pageable);

    return pagedItems.isEmpty() ? SearchPageGetResponse.of("검색 결과가 없습니다.", pagedItems)
        : SearchPageGetResponse.of(
            String.format("'%s'로 총 %s개의 검색결과를 찾았습니다.",
                searchKey,
                pagedItems.getTotalElements()), pagedItems);
  }

  public SearchPageGetResponse<TrackGetResponse> getEveryTrack(Pageable pageable,
      String memberEmail) {
    return getEveryItem(pageable, memberEmail, new TracksSearchTool(trackRepository));
  }

  public SearchPageGetResponse<SearchMembersResponse> getEveryMember(Pageable pageable,
      String memberEmail) {
    return getEveryItem(pageable, memberEmail, new MembersSearchTool(memberRepository));
  }

  public SearchPageGetResponse<TrackGetResponse> findTracksWithKeyword(Pageable pageable,
      String searchKey, String memberEmail) {
    return findWithKeyword(pageable, searchKey, memberEmail, new TracksSearchTool(trackRepository));
  }

  public SearchPageGetResponse<SearchMembersResponse> findMembersWithKeyword(Pageable pageable,
      String searchKey, String memberEmail) {
    return findWithKeyword(pageable, searchKey, memberEmail, new MembersSearchTool(memberRepository));
  }

  public SearchPageGetResponse<SearchRecommendResponse> findRecommendsWithKeyword(Pageable pageable,
      String searchKey, String memberEmail) {
    return findWithKeyword(pageable, searchKey, memberEmail, new RecommendsSearchTool(recommendRepository));
  }
}