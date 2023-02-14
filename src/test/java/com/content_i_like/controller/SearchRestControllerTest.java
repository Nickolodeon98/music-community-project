package com.content_i_like.controller;

import com.content_i_like.config.JwtService;
import com.content_i_like.controller.restcontroller.SearchRestController;
import com.content_i_like.domain.dto.search.SearchRecommendsResponse;
import com.content_i_like.domain.dto.search.SearchMembersResponse;
import com.content_i_like.domain.dto.search.SearchPageGetResponse;
import com.content_i_like.domain.dto.tracks.TrackGetResponse;
import com.content_i_like.domain.entity.Recommend;
import com.content_i_like.fixture.Fixture;
import com.content_i_like.service.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SearchRestController.class)
@WithMockUser
class SearchRestControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  SearchService searchService;

  @MockBean
  JwtService jwtService;

  @MockBean
  UserDetailsService userDetailsService;
  @Captor
  ArgumentCaptor<Pageable> argumentCaptor;
  final String BASE_URL = "/api/v1/search/";
  TrackGetResponse foundTrack;
  Page<TrackGetResponse> pagedTracks;
  SearchPageGetResponse<TrackGetResponse> pagedTracksWithMessage;
  SearchMembersResponse member;
  SearchPageGetResponse<SearchMembersResponse> membersPage;
  final String nickName = "nickname";
  final String recommendTitle = "title";
  Recommend recommend;
  SearchRecommendsResponse searchedRecommends;
  SearchPageGetResponse<SearchRecommendsResponse> pagedRecommends;
  Pageable setPageable(String sortCondition) {
    return PageRequest.of(0, 10, Sort.by(sortCondition).descending());
  }

  @BeforeEach
  void setUp() {
    foundTrack = TrackGetResponse.builder()
        .trackTitle("Event Horizon")
        .trackArtist("Younha")
        .trackAlbum("YOUNHA 6th Album Repackage 'END THEORY : Final Edition'")
        .build();
    pagedTracks = new PageImpl<>(List.of(foundTrack));

    pagedTracksWithMessage = SearchPageGetResponse
        .of("총 " + pagedTracks.getTotalElements() + "개의 음원을 찾았습니다.", pagedTracks);

    member = SearchMembersResponse
        .builder()
        .nickName(nickName)
        .profileImgUrl("profileimagurl")
        .createdAt(LocalDateTime.now())
        .build();

    /* TrackPageGetResponse 랑 비슷하니까 인터페이스 적용 가능 보류 */
    membersPage = SearchPageGetResponse.of("총 1명의 사용자를 찾았습니다.", new PageImpl<>(List.of(member)));

    recommend = Fixture.getRecommendFixture(
        Fixture.getMemberFixture(),
        Fixture.getTrackFixture(
            Fixture.getAlbumFixture(
                Fixture.getArtistFixture())));

    searchedRecommends = SearchRecommendsResponse.of(recommend, recommend.getRecommendContent());

    pagedRecommends =
        SearchPageGetResponse.of("총 1개의 검색결과를 찾았습니다.", new PageImpl<>(List.of(searchedRecommends)));
  }

  @Nested
  @DisplayName("모든 음원 검색")
  class AllTracksSearch {

    @Test
    @DisplayName("성공")
    void success_get_every_track() throws Exception {
      given(searchService.getEveryTrack(eq(setPageable("trackNo")), any())).willReturn(
          pagedTracksWithMessage);

      mockMvc.perform(get(BASE_URL + "tracks").with(csrf()))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
          .andExpect(jsonPath("$.result.pages.content").exists())
          .andDo(print());

      verify(searchService).getEveryTrack(argumentCaptor.capture(), any());

      Pageable actualPageable = argumentCaptor.getValue();
      assertEquals(setPageable("trackNo"), actualPageable);
    }
  }

  @Nested
  @DisplayName("검색어에 해당되는 음원 검색")
  class CertainTracksSearch {

    @Test
    @DisplayName("성공")
    void success_search_by_keyword() throws Exception {
      String searchKey = "Horizon";
      given(searchService.findTracksWithKeyword(eq(setPageable("trackNo")), eq(searchKey))).willReturn(pagedTracksWithMessage);

      mockMvc.perform(get(BASE_URL + "tracks/" + searchKey).with(csrf()))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
          .andExpect(jsonPath("$.result.pages.content[0].trackTitle").value("Event Horizon"))
          .andExpect(jsonPath("$.result.pages.content[0].trackArtist").value("Younha"))
          .andExpect(jsonPath("$.result.pages.content[0].trackAlbum")
              .value("YOUNHA 6th Album Repackage 'END THEORY : Final Edition'"))
          .andDo(print());

      verify(searchService).findTracksWithKeyword(eq(setPageable("trackNo")), eq(searchKey));
    }
  }

  @Nested
  @DisplayName("모든 사용자 검색")
  class AllMembersSearch {

    @Test
    @DisplayName("성공")
    void success_search_all_members() throws Exception {
      /* Authentication 객체, pageable 객체(slice 는 보류사항)  */
      given(searchService.getEveryMember(eq(setPageable("createdAt")), any())).willReturn(
          membersPage);

      mockMvc.perform(get(BASE_URL + "members").with(csrf()))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
          .andDo(print());

      verify(searchService).getEveryMember(eq(setPageable("createdAt")), any());
    }
  }

  @Nested
  @DisplayName("검색어에 해당되는 사용자 검색")
  class CertainMembersSearch {

    @Test
    @DisplayName("성공")
    void success_search_by_keyword() throws Exception {
      given(searchService.findMembersWithKeyword(eq(setPageable("createdAt")), eq(nickName))).willReturn(membersPage);

      mockMvc.perform(get(BASE_URL + "members/" + nickName).with(csrf()))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.result.message").value("총 1명의 사용자를 찾았습니다."))
          .andDo(print());

      verify(searchService).findMembersWithKeyword(eq(setPageable("createdAt")), eq(nickName));
    }
  }

  @Nested
  @DisplayName("검색어에 해당되는 추천글 검색")
  class RecommendsSearch {

    @Test
    @DisplayName("성공")
    void success_search_recommends_by_keyword() throws Exception {
      given(searchService.findRecommendsWithKeyword(eq(setPageable("recommendNo")),eq(recommendTitle)))
          .willReturn(pagedRecommends);

      mockMvc.perform(get(BASE_URL + "/recommends/" + recommendTitle).with(csrf()))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
          .andDo(print());

      verify(searchService).findRecommendsWithKeyword(eq(setPageable("recommendNo")),eq(recommendTitle));
    }

    @Test
    @DisplayName("성공 - 사용자 이름으로 검색")
    void success_search_recommends_by_nickname() throws Exception {
      given(searchService.findRecommendsWithMemberInfo(eq(setPageable("recommendNo")), eq(nickName)))
          .willReturn(pagedRecommends);

      mockMvc.perform(get(BASE_URL + "recommends/" + nickName).with(csrf()))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
          .andExpect(jsonPath("$.result.content").exists())
          .andDo(print());

      verify(searchService).findRecommendsWithMemberInfo(eq(setPageable("recommendsNo")), eq(nickName));
    }
  }
}