package com.content_i_like.service;

import com.content_i_like.domain.dto.recommend.*;
import com.content_i_like.domain.entity.*;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.*;
import java.util.ArrayList;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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
    Recommend recommend = saveRecommend(request, member, track, url);
    savePostHashtags(hashtags, recommend);
    return RecommendPostResponse.fromEntity(recommend);
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
    if (image != null) {
      return s3FileUploadService.uploadFile(image);
    }
    return "https://content-i-like.s3.ap-northeast-2.amazonaws.com/default-recommend.jpg";
  }

  /**
   * 해시태그를 검증하고 저장합니다.
   *
   * @param hashtags  웹에서 받아온 hashtags
   * @param recommend 해시태그를 저장할 추천글
   */
  private void savePostHashtags(List<String> hashtags, Recommend recommend) {
    for (String hashtag : hashtags) {
      Hashtag existingHashtag = hashtagRepository.findByName(hashtag)
          .orElseGet(() -> {
            Hashtag newHashtag = Hashtag.of(hashtag);
            hashtagRepository.save(newHashtag);
            return newHashtag;
          });

      PostHashtag postHashtag = PostHashtag.of(recommend, existingHashtag);
      postHashtagRepository.save(postHashtag);
    }
  }

  /**
   * 등록된 추천글을 수정합니다.
   *
   * @param userEmail   수정을 요청한 사용자 email
   * @param recommendNo 수정할 추천글 고유 번호
   * @param request     수정할 추천글 정보
   * @param image
   * @param hashtags
   * @return 수정된 추천글 내용
   */
  @Transactional
  public RecommendModifyResponse modifyPost(final String userEmail, final Long recommendNo,
      final RecommendModifyRequest request, MultipartFile image, List<String> hashtags)
      throws IOException {

    // get member
    Member member = validateGetMemberInfoByUserEmail(userEmail);
    // get recommend
    Recommend recommend = validateGetRecommendInfoByRecommendNo(recommendNo);
    // validate Member, Recommend
    validateMemberMatchInRecommend(member, recommend);
    // get image url
    String url = getModifyImageURL(image, recommend);

    // 수정 게시물
    recommendRepository.update(request.getRecommendTitle(), request.getRecommendContent(),
        url, request.getRecommendYoutubeUrl(), recommendNo);

    recommend = validateGetRecommendInfoByRecommendNo(recommendNo);

    // 해시태그
    updateAndManageHashtags(recommendNo, hashtags, recommend);

    return new RecommendModifyResponse(recommend.getRecommendNo(), recommend.getRecommendTitle());
  }

  private void updateAndManageHashtags(Long recommendNo, List<String> hashtags,
      Recommend recommend) {
    List<PostHashtag> existingPostHashtags = postHashtagRepository.findAllByRecommendRecommendNo(
        recommendNo);
    List<String> existingHashtags = getExistingHashtags(existingPostHashtags);

    // 두 해시태그가 동일하면 패스, 다르면 이전 해시태그 모두 삭제, 새로운 해시태그 등록.
    updateHashtags(hashtags, recommend, existingHashtags);
  }

  private void validateMemberMatchInRecommend(Member member, Recommend recommend) {
    if (!Objects.equals(member.getMemberNo(), recommend.getMember().getMemberNo())) {
      throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
    }
  }

  private void updateHashtags(List<String> hashtags, Recommend recommend,
      List<String> existingHashtags) {
    if (!new HashSet<>(existingHashtags).equals(
        new HashSet<>(hashtags))) {
      // Hash 태그 삭제
      postHashtagRepository.deleteAllByRecommend(recommend);
      savePostHashtags(hashtags, recommend);
    }
  }

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

  private String getModifyImageURL(MultipartFile image, Recommend recommend) throws IOException {
    if (image.isEmpty()) {
      return recommend.getRecommendImageUrl();
    }
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
    // 글을 작성하는 Member 확인
    Member member = validateGetMemberInfoByUserEmail(userEmail);

    // 수정 글 확인
    Recommend recommend = validateGetRecommendInfoByRecommendNo(recommendNo);

    // Member와 Recommen의 작성자가 동일한지 확인
    if (!Objects.equals(member.getMemberNo(), recommend.getMember().getMemberNo())) {
      throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
    }

    recommendRepository.delete(recommend);
  }

  /**
   * 추천글의 정보를 받아옵니다.
   *
   * @param recommendNo 정보를 받아올 추천글 고유번호
   * @return 추천 글의 정보를 반환합니다.
   */
  public RecommendReadResponse readPost(final Long recommendNo) {
    // 해당 글을 불러 옵니다.
    Recommend post = validateGetRecommendInfoByRecommendNo(recommendNo);

    // 해당 글의 작성자 정보를 받아옵니다.
    Member member = validateGetMemberInfoByUserEmail(post.getMember().getEmail());

    // 해당 글의 음악 정보를 받아옵니다.
    Track track = validateGetTrackByTrackNo(post.getTrack().getTrackNo());

    // 앨범 정보를 받아옵니다.
    Album album = albumRepository.findById(track.getAlbum().getAlbumNo())
        .orElseThrow(() -> {
          throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
        });

    // 아티스트의 정보를 받아옵니다.
    Artist artist = artistRepository.findById(album.getArtist().getArtistNo())
        .orElseThrow(() -> {
          throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
        });

    // 해당 글에 속해있는 comment 목록
    List<Comment> comments = post.getComments();

    // 좋아요 총 합
    Long countLikes = (long) post.getLikes().size();

    // 댓글과 게시물의 포인트 총 합
    Long accumulatedPoints = comments.stream()
        .mapToLong(Comment::getCommentPoint)
        .sum();

    return RecommendReadResponse.builder()
        .recommendTitle(post.getRecommendTitle())
        .memberNickname(member.getNickName())
        .albumImageUrl(album.getAlbumImageUrl())
        .trackTitle(track.getTrackTitle())
        .artistName(artist.getArtistName())
        .comments(comments)
        .recommendContent(post.getRecommendContent())
        .countLikes(countLikes)
        .recommendPoint(post.getRecommendPoint())
        .accumulatedPoints(accumulatedPoints)
        .recommendYoutubeUrl(post.getRecommendYoutubeUrl())
        .build();
  }

  private Recommend validateGetRecommendInfoByRecommendNo(final Long recommendNo) {
    return recommendRepository.findById(recommendNo)
        .orElseThrow(() -> {
          throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
        });
  }

  private Member validateGetMemberInfoByUserEmail(final String userEmail) {
    return memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> {
          throw new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
        });
  }

  public Page<RecommendListResponse> getPostList(final Pageable pageable) {
    return recommendRepository.findAll(pageable)
        .map(RecommendListResponse::of);
  }
}
