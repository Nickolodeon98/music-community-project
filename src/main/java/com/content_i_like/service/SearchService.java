package com.content_i_like.service;

import static com.content_i_like.service.validchecks.ArbitraryValidationService.validate;

import com.content_i_like.domain.dto.SortStrategy;
import com.content_i_like.domain.dto.search.SearchMembersResponse;
import com.content_i_like.domain.dto.search.SearchPageGetResponse;
import com.content_i_like.domain.dto.search.SearchRecommendsResponse;
import com.content_i_like.domain.dto.tracks.TrackGetResponse;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.enums.SortEnum;
import com.content_i_like.repository.MemberRepository;
import com.content_i_like.repository.RecommendRepository;
import com.content_i_like.repository.TrackRepository;
import com.content_i_like.service.searchtools.ItemSearch;
import com.content_i_like.service.searchtools.MembersSearchTool;
import com.content_i_like.service.searchtools.RecommendsByMembersSearchTool;
import com.content_i_like.service.searchtools.RecommendsSearchTool;
import com.content_i_like.service.searchtools.TracksSearchTool;
import com.content_i_like.service.validchecks.MemberValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {
  private final TrackRepository trackRepository;
  private final MemberRepository memberRepository;
  private final RecommendRepository recommendRepository;

  public Pageable generatePageable(Pageable pageable, SortStrategy sortStrategy) {
    String property = SortEnum.TRACKS_SORT_DEFAULT.getSortBy();
    Direction direction = SortEnum.TRACKS_SORT_DEFAULT.getDirection();

    if (sortStrategy != null) {
      if (sortStrategy.getProperty() != null && !sortStrategy.getProperty().isEmpty()) {
        property = sortStrategy.getProperty();
      }
      if (sortStrategy.getDirection() != null) {
        direction = sortStrategy.getDirection();
      }
    }

    return PageRequest.of(pageable.getPageNumber(), SortEnum.TRACKS_SORT_DEFAULT.getScale(),
        Sort.by(direction, property));
  }

  public <T> SearchPageGetResponse<T> getEveryItem(Pageable pageable, String memberEmail,
      ItemSearch<T> searchTool) {
    Member member = validate(memberEmail, new MemberValidation(memberRepository));
    Page<T> pagedItems = searchTool.searchAll(pageable);

    String message = searchTool.buildMessage();

    return SearchPageGetResponse.of(
        String.format("총 %s개의 %s 찾았습니다.", pagedItems.getTotalElements(), message), pagedItems);
  }

  public <T> SearchPageGetResponse<T> findWithKeyword(Pageable pageable, String searchKey, ItemSearch<T> searchTool) {
//    Member member = validate(memberEmail, new MemberValidation(memberRepository));

    Page<T> pagedItems = searchTool.search(searchKey, pageable);

    String message = searchTool.buildMessage();

    return pagedItems.isEmpty() ?
        SearchPageGetResponse.of(message.substring(0,message.length()-1) + " 검색 결과가 없습니다.", pagedItems)
        : SearchPageGetResponse.of(
            String.format("'%s' 으로 총 %s개의 %s 찾았습니다.",
                searchKey,
                pagedItems.getTotalElements(), message), pagedItems);
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
      String searchKey) {
    return findWithKeyword(pageable, searchKey, new TracksSearchTool(trackRepository));
  }

  public SearchPageGetResponse<SearchMembersResponse> findMembersWithKeyword(Pageable pageable,
      String searchKey) {
    return findWithKeyword(pageable, searchKey, new MembersSearchTool(memberRepository));
  }

  public SearchPageGetResponse<SearchRecommendsResponse> findRecommendsWithKeyword(Pageable pageable,
      String searchKey) {
    return findWithKeyword(pageable, searchKey, new RecommendsSearchTool(recommendRepository));
  }

  public SearchPageGetResponse<SearchRecommendsResponse> findRecommendsWithMemberInfo(Pageable pageable,
      String searchKey) {
    return findWithKeyword(pageable, searchKey, new RecommendsByMembersSearchTool(recommendRepository));
  }
}