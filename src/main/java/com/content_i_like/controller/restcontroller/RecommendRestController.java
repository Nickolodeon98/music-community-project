package com.content_i_like.controller.restcontroller;

import com.content_i_like.domain.Response;
import com.content_i_like.domain.dto.recommend.RecommendDeleteResponse;
import com.content_i_like.domain.dto.recommend.RecommendListResponse;
import com.content_i_like.domain.dto.recommend.RecommendModifyRequest;
import com.content_i_like.domain.dto.recommend.RecommendModifyResponse;
import com.content_i_like.domain.dto.recommend.RecommendPostRequest;
import com.content_i_like.domain.dto.recommend.RecommendPostResponse;
import com.content_i_like.domain.dto.recommend.RecommendReadResponse;
import com.content_i_like.service.RecommendService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/recommends")
@RequiredArgsConstructor
@Slf4j
public class RecommendRestController {

  private final RecommendService recommendService;

  /**
   * 추천 글을 작성합니다.
   *
   * @param authentication header의 token
   * @param request        등록할 추천글 정보
   * @return 등록된 추천글 정보
   */
  @PostMapping
  public Response<RecommendPostResponse> uploadRecommendPost(final Authentication authentication,
      @RequestPart(required = false, name = "image") MultipartFile image,
      @RequestPart(name = "request") @Valid final RecommendPostRequest request,
      @RequestPart(required = false, name = "hashtag1") final String hashtag1,
      @RequestPart(required = false, name = "hashtag2") final String hashtag2,
      @RequestPart(required = false, name = "hashtag3") final String hashtag3) throws IOException {
    String userEmail = authentication.getName();
    log.info("user_email = {}, recommend_post_request = {}, image = {}", userEmail, request, image);

    List<String> hashtags = Stream.of(hashtag1, hashtag2, hashtag3)
        .distinct()
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

    RecommendPostResponse response = recommendService.uploadPost(userEmail, request, image,
        hashtags);
    return Response.success(response);
  }

  /**
   * 등록된 추천글을 수정합니다.
   *
   * @param authentication header의 token
   * @param request        수정할 추천글 정보
   * @param recommendNo    수정할 추천글 고유 번호
   * @return 수정된 추천글 내용
   */
  @PostMapping("/{recommendNo}/update")
  public Response<RecommendModifyResponse> modifyRecommendPost(final Authentication authentication,
      @RequestPart(required = false, name = "image") MultipartFile image,
      @RequestPart(name = "request") @Valid final RecommendModifyRequest request,
      @RequestPart(required = false, name = "hashtag1") final String hashtag1,
      @RequestPart(required = false, name = "hashtag2") final String hashtag2,
      @RequestPart(required = false, name = "hashtag3") final String hashtag3,
      @PathVariable final Long recommendNo) throws IOException {
    String userEmail = authentication.getName();
    log.info("user_email = {}, recommend_modify_request = {}", userEmail, request);

    List<String> hashtags = Stream.of(hashtag1, hashtag2, hashtag3)
        .distinct()
        .filter(Objects::nonNull).toList();

    RecommendModifyResponse response = recommendService.modifyPost(userEmail, recommendNo, request,
        image, hashtags);
    return Response.success(response);
  }

  /**
   * 등록된 추천 글을 삭제합니다.
   *
   * @param authentication header의 token
   * @param recommendNo    삭제할 추천글 고유번호
   * @return 삭제 결과
   */
  @DeleteMapping("/{recommendNo}")
  public Response<RecommendDeleteResponse> deleteRecommendPost(final Authentication authentication,
      @PathVariable final Long recommendNo) {
    String userEmail = authentication.getName();
    log.info("user email = {}, recommend_no = {}", userEmail, recommendNo);

    recommendService.deletePost(userEmail, recommendNo);
    return Response.success(new RecommendDeleteResponse(recommendNo, "추천 글이 삭제 되었습니다."));
  }

  /**
   * 추천글의 정보를 받아옵니다.
   *
   * @param recommendNo 정보를 받아올 추천글 고유번호
   * @return 추천글의 정보를 반환합니다.
   */
  @GetMapping("/{recommendNo}")
  public Response<RecommendReadResponse> ReadRecommendPost(@PathVariable final Long recommendNo) {
    log.info("recommend_no = {}", recommendNo);

    RecommendReadResponse response = recommendService.readPost(recommendNo);
    return Response.success(response);
  }


  @GetMapping// local/api/v1/recommend?sort=recommendNo; "게시글순" name = sort, value = "recommendNo"
  public Response<Page<RecommendListResponse>> ReadRecommendPost(
      @RequestParam(required = false, defaultValue = "recommendTitle") String sort) {
    Pageable pageable = PageRequest.of(0, 20, Sort.by(sort).ascending());

    log.info("recommend_no = {}", sort);

    Page<RecommendListResponse> response = recommendService.getPostList(pageable, "createdAt");
    return Response.success(response);
  }
}
