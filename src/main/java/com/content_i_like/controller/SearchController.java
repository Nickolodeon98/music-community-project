package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.search.SearchMembersResponse;
import com.content_i_like.domain.dto.search.SearchPageGetResponse;
import com.content_i_like.domain.dto.search.SearchRecommendsResponse;
import com.content_i_like.domain.dto.tracks.TrackGetResponse;
import com.content_i_like.service.CacheService;
import com.content_i_like.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
@Slf4j
public class SearchController {

  private final SearchService searchService;
  private final CacheService cacheService;
  @GetMapping("/tracks")
  public Response<SearchPageGetResponse<TrackGetResponse>> searchAllTracks(
      final Authentication authentication,
      @RequestParam(defaultValue = "trackTitle") String sort) {

    Pageable pageable = PageRequest.of(0, 20, Sort.by(sort).descending());

    SearchPageGetResponse<TrackGetResponse> searchResults =
        searchService.getEveryTrack(pageable, authentication.getName());

    return Response.success(searchResults);
  }

  @GetMapping("/tracks/{trackTitle}")
  public Response<SearchPageGetResponse<TrackGetResponse>> searchTracksByKeyword(
      final Authentication authentication,
      @PathVariable final String trackTitle,
      @RequestParam(defaultValue = "trackTitle") String sort) {

    Pageable pageable = PageRequest.of(0, 20, Sort.by(sort).descending());

    SearchPageGetResponse<TrackGetResponse> searchResults =
        searchService.findTracksWithKeyword(pageable, trackTitle, authentication.getName());

    return Response.success(searchResults);
  }

  @GetMapping("/members")
  public Response<SearchPageGetResponse<SearchMembersResponse>> searchAllMembers(
      final Authentication authentication,
      @RequestParam(required = false, defaultValue = "createdAt") String sort) {

    Pageable pageable = PageRequest.of(0, 20, Sort.by(sort).descending());

    SearchPageGetResponse<SearchMembersResponse> searchedMembers =
        searchService.getEveryMember(pageable, authentication.getName());

    return Response.success(searchedMembers);
  }

  @GetMapping("/members/{nickName}")
  public Response<SearchPageGetResponse<SearchMembersResponse>> searchMembersByKeyword(
      final Authentication authentication,
      @PathVariable final String nickName,
      @RequestParam(required = false, defaultValue = "createdAt") String sort) {

    Pageable pageable = PageRequest.of(0, 20, Sort.by(sort).descending());

    SearchPageGetResponse<SearchMembersResponse> searchedMembers =
        searchService.findMembersWithKeyword(pageable, nickName, authentication.getName());

    return Response.success(searchedMembers);
  }

  @GetMapping("/recommends/{recommendTitle}")
  public Response<SearchPageGetResponse<SearchRecommendsResponse>> searchRecommendsByKeyword(
      final Authentication authentication,
      @PathVariable final String recommendTitle,
      @RequestParam(required = false, defaultValue = "createdAt") String sort) {

    Pageable pageable = PageRequest.of(0, 20, Sort.by(sort).descending());

    SearchPageGetResponse<SearchRecommendsResponse> pagedResponseRecommends =
        searchService.findRecommendsWithKeyword(pageable, recommendTitle, authentication.getName());

    return Response.success(pagedResponseRecommends);
  }

  @GetMapping("/recommends/{memberNickName}")
  public Response<SearchPageGetResponse<SearchRecommendsResponse>> searchRecommendsByKeywordOfMemberNickName(
      final Authentication authentication,
      @PathVariable final String memberNickName,
      @RequestParam(required = false, defaultValue = "createdAt") String sort) {

    Pageable pageable = PageRequest.of(0, 20, Sort.by(sort).descending());

    SearchPageGetResponse<SearchRecommendsResponse> pagedResponseRecommends =
        searchService.findRecommendsWithMemberInfo(pageable, memberNickName, authentication.getName());

    return Response.success(pagedResponseRecommends);
  }
}