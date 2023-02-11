package com.content_i_like.service;

import com.content_i_like.domain.dto.recommend.*;
import com.content_i_like.domain.entity.*;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RecommendService {

  private final MemberRepository memberRepository;
  private final RecommendRepository recommendRepository;

  private final AlbumRepository albumRepository;
  private final ArtistRepository artistRepository;
  private final TrackRepository trackRepository;
  private final S3FileUploadService s3FileUploadService;
  private final HashtagRepository hashtagRepository;
  private final PostHashtagRepository postHashtagRepository;


  /**
   * 추천 글을 작성합니다.
   *
   * @param userEmail 작성자의 email
   * @param request   등록할 추천글 정보
   * @param image     등록할 이미지 정보
   * @param hashtags  추천글에 작성된 해시태그
   * @return 등록된 추천글 정보
   * @throws IOException
   */
  @Transactional
  public RecommendPostResponse uploadPost(final String userEmail,
      final RecommendPostRequest request,
      MultipartFile image, List<String> hashtags) throws IOException {

    Member member = validateGetMemberInfoByUserEmail(userEmail);
    Track track = validateGetTrackByTrackNo(request.getTrackNo());
    String url = getUploadImageURL(image);
    Recommend post = saveRecommend(request, member, track, url);
    savePostHashtags(hashtags, post);
    return RecommendPostResponse.fromEntity(post);
  }

  @Transactional
  public RecommendPostResponse uploadPost(final String userEmail,
      final RecommendPostRequest request) throws IOException {
    Member member = validateGetMemberInfoByUserEmail(userEmail);
//    Track track = validateGetTrackByTrackNo(request.getTrackNo());
    Track track = validateGetTrackByTrackNo(1L);
    String url = getUploadImageURL(request.getImage());
    Recommend post = saveRecommend(request, member, track, url);
    saveHashTags(request, post);
    return RecommendPostResponse.fromEntity(post);
  }

  private void saveHashTags(RecommendPostRequest request, Recommend post) {
    Optional.ofNullable(request.getHashtag())
        .filter(str -> str.contains("#"))
        .map(str -> Stream.of(str.split("#"))
            .map(h -> h.replaceAll(" ", ""))
            .filter(h -> !h.isEmpty())
            .distinct()
            .toList()).ifPresent(hashtags -> savePostHashtags(hashtags, post));
  }

  /**
   * 추천글을 Database에 저장합니다.
   *
   * @param request 등록할 추천글 정보
   * @param member  추천글을 작성하는 작성자
   * @param track   추천글의 음원 정보
   * @param url     추천글 썸네일 이미지 주소
   * @return 저장된 추천글 엔티티 반환
   */
  private Recommend saveRecommend(RecommendPostRequest request, Member member, Track track,
      String url) {
    return recommendRepository.save(
        RecommendPostRequest.toEntity(request, member, track, url));
  }

  /**
   * 트랙 정보를 검증하고 받아옵니다.
   *
   * @param trackNo 찾고자 하는 트랙의 고유번호
   * @return 트랙 정보를 반환합니다.
   */
  private Track validateGetTrackByTrackNo(Long trackNo) {
    return trackRepository.findById(trackNo)
        .orElseThrow(() -> {
          throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
        });
  }

  /**
   * 이미지를 업로드하고, 업로드 된 URL을 반환합니다.
   *
   * @param image 업로드 된 image 파일
   * @return image url을 반환
   * @throws IOException
   */
  private String getUploadImageURL(MultipartFile image) throws IOException {
    if (!image.isEmpty()) {
      return s3FileUploadService.uploadFile(image);
    }
    return "https://content-i-like.s3.ap-northeast-2.amazonaws.com/default-recommend.jpg";
  }

  /**
   * 해시태그를 검증하고 저장합니다.
   *
   * @param hashtags 웹에서 받아온 hashtags
   * @param post     해시태그를 저장할 추천글
   */
  private void savePostHashtags(List<String> hashtags, Recommend post) {
    for (String hashtag : hashtags) {
      Hashtag existingHashtag = hashtagRepository.findByName(hashtag)
          .orElseGet(() -> {
            Hashtag newHashtag = Hashtag.of(hashtag);
            hashtagRepository.save(newHashtag);
            return newHashtag;
          });

      PostHashtag postHashtag = PostHashtag.of(post, existingHashtag);
      postHashtagRepository.save(postHashtag);
    }
  }

  /**
   * 등록된 추천글을 수정합니다.
   *
   * @param userEmail   수정을 요청한 사용자 email
   * @param recommendNo 수정할 추천글 고유 번호
   * @param request     수정할 추천글 정보
   * @return 수정된 추천글 내용
   */
  @Transactional
  public RecommendModifyResponse modifyPost(final String userEmail, final Long recommendNo,
      final RecommendModifyRequest request)
      throws IOException {

    Member member = validateGetMemberInfoByUserEmail(userEmail);
    Recommend post = validateGetRecommendInfoByRecommendNo(recommendNo);
    validateMemberMatchInRecommend(member, post);
    String url = getModifyImageURL(request.getImage(), post);
    updatePost(recommendNo, request, url);
    post = validateGetRecommendInfoByRecommendNo(recommendNo);

    List<String> hashtags = getHashtagsFromRequest(request);
    updateAndManageHashtags(post, hashtags);
    return new RecommendModifyResponse(post.getRecommendNo(), post.getRecommendTitle());
  }

  private List<String> getHashtagsFromRequest(RecommendModifyRequest request) {
    List<String> hashtags = Optional.ofNullable(request.getHashtag())
        .filter(str -> str.contains("#"))
        .map(str -> Stream.of(str.split("#"))
            .map(h -> h.replaceAll(" ", ""))
            .filter(h -> !h.isEmpty())
            .distinct()
            .toList())
        .orElse(null);
    return hashtags;
  }

  /**
   * 추천글을 수정합니다.
   *
   * @param recommendNo 수정할 추천글 고유 번호
   * @param request     수정할 추천글 정보
   * @param url         수정할 추천글의 이미지 url
   */
  private void updatePost(Long recommendNo, RecommendModifyRequest request, String url) {
    String youtubeUrl = getYoutubeURL(request);

    recommendRepository.update(request.getRecommendTitle(), request.getRecommendContent(),
        url, youtubeUrl, recommendNo);
  }

  private String getYoutubeURL(RecommendModifyRequest request) {
    String youtubeUrl = "https://youtu.be/";
    if (request.getRecommendYoutubeUrl() != null) {
      youtubeUrl += request.getRecommendYoutubeUrl();
    }
    return youtubeUrl;
  }

  /**
   * 추천글에 입력된 해시태그를 검증하고 수정합니다.
   *
   * @param hashtags  수정할 추천글 해시태그
   * @param post 수정할 추천글
   */
  private void updateAndManageHashtags(Recommend post, List<String> hashtags) {

    if (hashtags == null) {
      postHashtagRepository.deleteAllByRecommend(post);
      return;
    }
    List<PostHashtag> existingPostHashtags = postHashtagRepository.findAllByRecommendRecommendNo(
        post.getRecommendNo());
    List<String> existingHashtags = getExistingHashtags(existingPostHashtags);

    // 두 해시태그가 동일하면 패스, 다르면 이전 해시태그 모두 삭제, 새로운 해시태그 등록.
    updateHashtags(hashtags, post, existingHashtags);
  }

  /**
   * 해당 글의 작성자와 해당 기능 요청자가 같은 사용자인지 검증합니다.
   *
   * @param member 사용자
   * @param post   추천 글
   */
  private void validateMemberMatchInRecommend(Member member, Recommend post) {
    if (!Objects.equals(member.getMemberNo(), post.getMember().getMemberNo())) {
      throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
    }
  }

  /**
   * 이미 추천 글에 존재하던 해시 태그와 수정된 글에 존재하는 해시 태그를 비교하고 두 값이 다를 시 기존의 해시태그를 삭제 후 새로운 해시 태그를 저장합니다. 두 값이
   * 동일하다면 해당 로직은 진행되지 않습니다.
   *
   * @param hashtags         수정하고자 하는 해시태그
   * @param post             수정 글
   * @param existingHashtags 이미 글에 존재하는 해시태그
   */
  private void updateHashtags(List<String> hashtags, Recommend post,
      List<String> existingHashtags) {
    if (!new HashSet<>(existingHashtags).equals(
        new HashSet<>(hashtags))) {
      // Hash 태그 삭제
      postHashtagRepository.deleteAllByRecommend(post);
      savePostHashtags(hashtags, post);
    }
  }

  /**
   * 추천글과 해시태그가 연결되어 있는 중간 테이블의 정보입니다. 기존 추천 글에 존재하는 해시 태그를 불러옵니다.
   *
   * @param existingPostHashtags postHashtag 정보
   * @return 기존 글에 저장되어 있는 해시태그를 리스트로 반환합니다.
   */
  private List<String> getExistingHashtags(List<PostHashtag> existingPostHashtags) {
    List<String> existingHashtags = new ArrayList<>();
    for (PostHashtag postHashtag : existingPostHashtags) {
      Hashtag hashtag = hashtagRepository.findById(postHashtag.getHashtag().getHashtagNo())
          .orElseThrow(
              () -> {
                throw new ContentILikeAppException(ErrorCode.NOT_FOUND,
                    ErrorCode.NOT_FOUND.getMessage());
              });
      existingHashtags.add(hashtag.getName());
    }
    return existingHashtags;
  }

  /**
   * 수정할 이미지의 url을 반환합니다. 이미지의 값이 null이라면 기존 등록된 url을 반환하고, 이미지의 값이 not null 이라면, 이미지를 새롭게 업로드합니다.
   *
   * @param image 이미지 정보
   * @param post  추천글 정보
   * @return 이미지 url 반환
   * @throws IOException
   */
  private String getModifyImageURL(MultipartFile image, Recommend post) throws IOException {
    String url = post.getRecommendImageUrl();
    if (image == null) {
      return url;
    }
    s3FileUploadService.deleteFile(url.split("/")[3]);
    return s3FileUploadService.uploadFile(image);
  }


  /**
   * 등록된 추천 글을 삭제합니다.
   *
   * @param userEmail   삭제를 요청한 사용자 email
   * @param recommendNo 삭제할 추천 글 고유번호
   */
  @Transactional
  public void deletePost(final String userEmail, final Long recommendNo) {
    Member member = validateGetMemberInfoByUserEmail(userEmail);
    Recommend post = validateGetRecommendInfoByRecommendNo(recommendNo);

    // Member와 Recommen의 작성자가 동일한지 확인
    if (!Objects.equals(member.getMemberNo(), post.getMember().getMemberNo())) {
      throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
    }

    recommendRepository.delete(post);
  }

  /**
   * 추천글의 정보를 받아옵니다.
   *
   * @param recommendNo 정보를 받아올 추천글 고유번호
   * @return 추천 글의 정보를 반환합니다.
   */
  public RecommendReadResponse readPost(final Long recommendNo) {
    Recommend post = validateGetRecommendInfoByRecommendNo(recommendNo);
    Member member = validateGetMemberInfoByUserEmail(post.getMember().getEmail());
    Track track = validateGetTrackByTrackNo(post.getTrack().getTrackNo());
    Album album = validateGetAlbumByAlbumNo(track);
    Artist artist = validateGetArtistByArtistNo(album);
    List<Comment> comments = post.getComments();
    Long countLikes = (long) post.getLikes().size();
    Long accumulatedPoints = getAccumulatedPoints(comments, post);
    List<PostHashtag> postHashtags = postHashtagRepository.findAllByRecommendRecommendNo(
        recommendNo);
    List<String> hashtags = getExistingHashtags(postHashtags);
    return RecommendReadResponse.of(post, member, album, artist, track, comments, countLikes,
        accumulatedPoints, hashtags);
  }


  /**
   * artistNo을 사용하여 Artist 정보를 받아옵니다.
   *
   * @param album Artist 고유 번호가 담겨 있는 앨범 정보
   * @return Artist 정보를 반환
   */
  private Artist validateGetArtistByArtistNo(Album album) {
    return artistRepository.findById(album.getArtist().getArtistNo())
        .orElseThrow(() -> {
          throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
        });
  }

  /**
   * AlbumNo를 사용하여 Album 정보를 받아옵니다.
   *
   * @param track Album 고유 번호가 담겨 있는 트랙 정보
   * @return Album 정보를 반환
   */
  private Album validateGetAlbumByAlbumNo(Track track) {
    return albumRepository.findById(track.getAlbum().getAlbumNo())
        .orElseThrow(() -> {
          throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
        });
  }

  /**
   * 게시글과 댓글에 있는 포인트를 모두 더합니다.
   *
   * @param comments 댓글 리스트
   * @return 댓글 포인트와 게시물 포인트의 합 반환
   */
  private long getAccumulatedPoints(List<Comment> comments, Recommend post) {
    return comments.stream()
        .mapToLong(Comment::getCommentPoint)
        .sum() + post.getRecommendPoint();
  }

  /**
   * 추천글 고유 번호를 통하여 추천글 정보를 받아옵니다.
   *
   * @param recommendNo 추천글 고유 번호
   * @return 추천글 정보 반환
   */
  private Recommend validateGetRecommendInfoByRecommendNo(final Long recommendNo) {
    return recommendRepository.findById(recommendNo)
        .orElseThrow(() -> {
          throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
        });
  }

  /**
   * 사용자 이메일을 통하여 사용자 정보를 받아옵니다.
   *
   * @param userEmail 사용자 이메일
   * @return 사용자 정보 반환
   */
  private Member validateGetMemberInfoByUserEmail(final String userEmail) {
    return memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> {
          throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
        });
  }

  /**
   * @param pageable
   * @param sort
   * @return
   */
  public Page<RecommendListResponse> getPostList(final Pageable pageable, String sort) {
    if (sort.equals("subscribe")) {
      Page<Object[]> result = recommendRepository.findAllWithLikeCount(pageable);
      return new PageImpl<>(RecommendListResponse.of(result), pageable, result.getTotalElements());
    }

    if (sort.equals("point")) {
      Page<Object[]> result = recommendRepository.findAllWithAccumulatedPoints(pageable);
      return new PageImpl<>(RecommendListResponse.of(result), pageable, result.getTotalElements());
    }

    Page<Object[]> result = recommendRepository.findAllWithLikeCount(pageable);
    return new PageImpl<>(RecommendListResponse.of(result), pageable, result.getTotalElements());
  }

  public Recommend findPostById(Long recommendNo) {
    return recommendRepository.findById(recommendNo).orElseThrow(
        ()->{
          throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
        }
    );
  }

  public String getHashtagsToString(Long recommendNo) {

    List<PostHashtag> postHashtags = postHashtagRepository.findAllByRecommendRecommendNo(
        validateGetRecommendInfoByRecommendNo(recommendNo).getRecommendNo());

    return "#" + postHashtags.stream().map(postHashtag -> postHashtag.getHashtag().getName())
        .collect(Collectors.joining(" #"));
  }
}
