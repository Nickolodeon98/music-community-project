package com.content_i_like.controller;

import com.content_i_like.domain.dto.search.SortStrategy;
import com.content_i_like.domain.dto.search.SearchMembersResponse;
import com.content_i_like.domain.dto.search.SearchPageGetResponse;
import com.content_i_like.domain.dto.search.SearchRecommendsResponse;
import com.content_i_like.domain.dto.search.SearchRequest;
import com.content_i_like.domain.dto.tracks.TrackGetResponse;
import com.content_i_like.domain.enums.SortEnum;
import com.content_i_like.service.SearchService;
import com.content_i_like.service.TrackService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/search")
@RequiredArgsConstructor
@Slf4j
public class SearchController {

  private final SearchService searchService;
  private final TrackService trackService;

  @GetMapping()
  public String searchMainPage(Model model) {

    SearchRequest searchKeyword = SearchRequest.builder().build();
    model.addAttribute("keywordDto", searchKeyword);

    SortStrategy sortStrategy = SortStrategy.builder().build();
    model.addAttribute("sortStrategy", sortStrategy);

    List<String> allProperties = List.of("createdAt", "trackTitle");
    model.addAttribute("allProperties", allProperties);

    return "pages/search/search-main";
  }

  @PostMapping("/tracks")
  public String searchTracksPage(Model model) {

    SortStrategy sorting = SortStrategy.builder().build();
    model.addAttribute("sortStrategy", sorting);

    return "redirect:/search/tracks";
  }

  @GetMapping("/tracks")
  public String searchTracksByKeyword(@ModelAttribute("keywordDto") final SearchRequest trackTitle,
      @ModelAttribute("sortStrategy") final SortStrategy sortStrategy,
      Pageable pageable,
      Model model) {

    pageable = searchService.generatePageable(pageable, sortStrategy);

    SearchPageGetResponse<TrackGetResponse> trackResults =
        searchService.findTracksWithKeyword(pageable, trackTitle.getKeyword());

    /* 만약 trackResults 가 비어있는 경우라면 스포티파이 API 를 호출하는 경로로 리다이렉트 후 다른 메서드를 사용한다
     * accessToken 을 받아야 하기 때문. */
    if (trackResults.getPages().isEmpty()) {
      return "redirect:http://localhost:8080/api/v1/test/token?option=unplanned&keyword="
          + trackTitle.getKeyword();
    }

    model.addAttribute("trackResults", trackResults);
    model.addAttribute("elements", trackResults.getPages().getNumberOfElements());
    model.addAttribute("totalPages", trackResults.getPages().getTotalPages());
    model.addAttribute("trackResultsAsList", trackResults.getPages().toList());
    model.addAttribute("pageable", pageable);
    model.addAttribute("keyword", trackTitle.getKeyword());

    SortStrategy sorting = SortStrategy.builder().build();
    model.addAttribute("sortStrategy", sorting);

    return "pages/search/tracks-search";
  }

  @GetMapping("/tracks/demand")
  public String addTracksOnDemand(@RequestParam String token,
      @RequestParam String keyword,
      @ModelAttribute("keywordDto") final SearchRequest trackTitle,
      @ModelAttribute("sortStrategy") final SortStrategy sortStrategy)
      throws JsonProcessingException {

    trackService.createDBOnDemand(token, keyword);

    return "redirect:/search/tracks?keyword=" + keyword + "&page=0";
  }

  @PostMapping("/members")
  public String searchMembersPage(Model model) {

    SortStrategy sorting = SortStrategy.builder().build();
    model.addAttribute("sortStrategy", sorting);

    return "redirect:/search/members";
  }

  @GetMapping("/members")
  public String searchMembersByKeyword(@ModelAttribute("keywordDto") final SearchRequest nickName,
      @ModelAttribute("sortStrategy") final SortStrategy sorting,
      Pageable pageable,
      Model model) {

    String property = SortEnum.MEMBERS_SORT_DEFAULT.getSortBy();
    Direction direction = SortEnum.MEMBERS_SORT_DEFAULT.getDirection();

    if (sorting != null) {
      if (sorting.getProperty() != null && !sorting.getProperty().isEmpty()) {
        property = sorting.getProperty();
      }
      if (sorting.getDirection() != null) {
        direction = sorting.getDirection();
      }
    }

    pageable = PageRequest.of(pageable.getPageNumber(), SortEnum.MEMBERS_SORT_DEFAULT.getScale(),
        Sort.by(direction, property));

    SearchPageGetResponse<SearchMembersResponse> searchedMembers =
        searchService.findMembersWithKeyword(pageable, nickName.getKeyword());

    model.addAttribute("keyword", nickName.getKeyword());
    model.addAttribute("memberNickName", searchedMembers);
    model.addAttribute("memberNickNameAsList", searchedMembers.getPages().toList());
    model.addAttribute("pageable", pageable);

    return "pages/search/members-search";
  }

  @PostMapping("/recommends")
  public String searchRecommendsPage(Model model) {

    SortStrategy sorting = SortStrategy.builder().build();
    model.addAttribute("sortStrategy", sorting);

    return "redirect:/search/recommends";
  }

  @GetMapping("/recommends")
  public String searchRecommendsByKeyword(
      @ModelAttribute("keywordDto") final SearchRequest recommendTitle,
      @ModelAttribute("sortStrategy") final SortStrategy sortStrategy,
      Pageable pageable,
      Model model) {

    String property = SortEnum.RECOMMENDS_SORT_DEFAULT.getSortBy();
    Direction direction = SortEnum.RECOMMENDS_SORT_DEFAULT.getDirection();

    if (sortStrategy == null) {
      log.info("null 값입니다.");
    } else {
      if (sortStrategy.getProperty() != null && !sortStrategy.getProperty().isEmpty()) {
        property = sortStrategy.getProperty();
      }
      if (sortStrategy.getDirection() != null) {
        direction = sortStrategy.getDirection();
      }
    }

    pageable = PageRequest.of(pageable.getPageNumber(), SortEnum.RECOMMENDS_SORT_DEFAULT.getScale(),
        Sort.by(direction, property));

    SearchPageGetResponse<SearchRecommendsResponse> pagedResponseRecommends =
        searchService.findRecommendsWithKeyword(pageable, recommendTitle.getKeyword());

    model.addAttribute("recommendsList", pagedResponseRecommends);
    model.addAttribute("recommendsListAsList", pagedResponseRecommends.getPages().toList());
    model.addAttribute("keyword", recommendTitle.getKeyword());
    model.addAttribute("pageable", pageable);

    return "pages/search/recommends-search";
  }

  @GetMapping("/all")
  public String searchAll(@ModelAttribute("keywordDto") final SearchRequest searchKeyword,
      @PageableDefault(size = 2) Pageable pageable,
      Model model) {

    SearchPageGetResponse<TrackGetResponse> trackResults =
        searchService.findTracksWithKeyword(pageable, searchKeyword.getKeyword());

    SearchPageGetResponse<SearchMembersResponse> searchedMembers =
        searchService.findMembersWithKeyword(PageRequest.of(0, 5), searchKeyword.getKeyword());

    SearchPageGetResponse<SearchRecommendsResponse> pagedResponseRecommends =
        searchService.findRecommendsWithKeyword(pageable, searchKeyword.getKeyword());

    model.addAttribute("tracks", trackResults);
    model.addAttribute("tracksAsList", trackResults.getPages().toList());
    model.addAttribute("members", searchedMembers);
    model.addAttribute("membersAsList", searchedMembers.getPages().toList());
    model.addAttribute("recommends", pagedResponseRecommends);
    model.addAttribute("keyword", searchKeyword.getKeyword());
    model.addAttribute("recommendsAsList", pagedResponseRecommends.getPages().toList());

    return "pages/search/search-all";
  }

  @GetMapping("/tracks/modal")
  @ResponseBody // indicates that the response should be serialized as JSON
  public SearchPageGetResponse<TrackGetResponse> searchTracksByKeywordFromModal(
      @RequestParam(name = "keyword") String keyword,
      Pageable pageable,
      @RequestParam(value = "page", required = false) Integer pageNum) {

    pageable = PageRequest.of(pageNum != null ? pageNum : 0, 5, Sort.by("trackTitle").ascending());

    SearchPageGetResponse<TrackGetResponse> trackResults = searchService.findTracksWithKeyword(
        pageable, keyword);

    return trackResults;
  }
}