package com.content_i_like.service;

import com.content_i_like.domain.dto.search.SearchMembersResponse;
import com.content_i_like.domain.dto.search.SearchPageGetResponse;
import com.content_i_like.domain.dto.tracks.TrackGetResponse;
import com.content_i_like.domain.dto.tracks.TrackPageGetResponse;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Track;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.MemberRepository;
import com.content_i_like.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SearchService {

  private final TrackRepository trackRepository;
  private final MemberRepository memberRepository;

  interface ValidCheck<T> {

    T examine(String toCheck);
  }

  class MemberValidation implements ValidCheck<Member> {

    @Override
    public Member examine(String toCheck) {
      return memberRepository.findByEmail(toCheck)
          .orElseThrow(() -> new ContentILikeAppException(ErrorCode.NOT_FOUND,
              ErrorCode.NOT_FOUND.getMessage()));
    }
  }

  public <T> T validate(String toCheck, ValidCheck<T> validCheck) {
    return validCheck.examine(toCheck);
  }

  interface ItemSearch<T> {

    Page<T> searchAll(Pageable pageable);

    Page<T> search(String keyword, Pageable pageable);
  }

  class TracksSearchTool implements ItemSearch<TrackGetResponse> {

    @Override
    public Page<TrackGetResponse> searchAll(Pageable pageable) {
      return trackRepository.findAll(pageable).map(TrackGetResponse::of);
    }

    @Override
    public Page<TrackGetResponse> search(String keyword, Pageable pageable) {
      return trackRepository.findAllByTrackTitleContaining(keyword, pageable)
          .map(trackPage -> trackPage.map(TrackGetResponse::of))
          .orElseGet(() -> new PageImpl<>(Collections.emptyList()));
    }
  }

  class MembersSearchTool implements ItemSearch<SearchMembersResponse> {

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

  public <T> SearchPageGetResponse<T> getEveryItem(Pageable pageable, String memberEmail,
      ItemSearch<T> searchTool) {
    Member member = validate(memberEmail, new MemberValidation());
    Page<T> pagedItems = searchTool.searchAll(pageable);

    return SearchPageGetResponse.of(
        String.format("총 %s명의 사용자를 찾았습니다.", pagedItems.getTotalElements()), pagedItems);
  }

  public SearchPageGetResponse<TrackGetResponse> getEveryTrack(Pageable pageable,
      String memberEmail) {
    return getEveryItem(pageable, memberEmail, new TracksSearchTool());
  }

  public SearchPageGetResponse<SearchMembersResponse> getEveryMember(Pageable pageable,
      String memberEmail) {
    return getEveryItem(pageable, memberEmail, new MembersSearchTool());
  }

  public <T> SearchPageGetResponse<T> findWithKeyword(Pageable pageable, String searchKey,
      String memberEmail, ItemSearch<T> searchTool) {
    Member member = validate(memberEmail, new MemberValidation());

    Page<T> pagedItems = searchTool.search(searchKey, pageable);

    return pagedItems.isEmpty() ? SearchPageGetResponse.of("검색 결과가 없습니다.", pagedItems)
        : SearchPageGetResponse.of(
            String.format("'%s'로 총 %s개의 검색결과를 찾았습니다.",
                searchKey,
                pagedItems.getTotalElements()), pagedItems);
  }

  public SearchPageGetResponse<SearchMembersResponse> findMembersWithKeyword(Pageable pageable,
      String searchKey, String memberEmail) {
    return findWithKeyword(pageable, searchKey, memberEmail, new MembersSearchTool());
  }

  public SearchPageGetResponse<TrackGetResponse> findTracksWithKeyword(Pageable pageable,
      String searchKey, String memberEmail) {
    return findWithKeyword(pageable, searchKey, memberEmail, new TracksSearchTool());
  }
}