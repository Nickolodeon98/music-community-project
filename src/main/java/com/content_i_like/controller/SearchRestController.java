package com.content_i_like.controller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.search.SearchMembersResponse;
import com.content_i_like.domain.dto.search.SearchPageGetResponse;
import com.content_i_like.domain.dto.tracks.TrackGetResponse;
import com.content_i_like.domain.dto.tracks.TrackPageGetResponse;
import com.content_i_like.domain.entity.Track;
import com.content_i_like.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchRestController {

    private final SearchService searchService;

    @GetMapping("/tracks")
    public Response<SearchPageGetResponse<TrackGetResponse>> searchAllTracks(final Authentication authentication,
                                                                           @PageableDefault(sort = "trackNo", direction = Sort.Direction.DESC) Pageable pageable) {

        SearchPageGetResponse<TrackGetResponse> searchResults =
                searchService.getEveryTrack(pageable, authentication.getName());

        return Response.success(searchResults);
    }

    @GetMapping("/tracks/{trackTitle}")
    public Response<SearchPageGetResponse<TrackGetResponse>> searchTracksByKeyword(final Authentication authentication,
                                                          @PathVariable String trackTitle,
                                                          @PageableDefault(sort = "trackNo", direction = Sort.Direction.DESC) Pageable pageable) {

        SearchPageGetResponse<TrackGetResponse> searchResults =
                searchService.findTracksWithKeyword(pageable, trackTitle, authentication.getName());

        return Response.success(searchResults);
    }

    @GetMapping("/members")
    public Response<SearchPageGetResponse<SearchMembersResponse>> searchAllMembers(final Authentication authentication,
                                                            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        SearchPageGetResponse<SearchMembersResponse> searchedMembers =
                searchService.getEveryMember(pageable, authentication.getName());

        return Response.success(searchedMembers);
    }

    @GetMapping("/members/{nickName}")
    public Response<SearchPageGetResponse<SearchMembersResponse>> searchMembersByKeyword(final Authentication authentication,
                                                                                         @PathVariable final String nickName,
                                                                                         @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        SearchPageGetResponse<SearchMembersResponse> searchedMembers =
                searchService.findMembersWithKeyword(pageable, nickName, authentication.getName());

        return Response.success(searchedMembers);
    }

}