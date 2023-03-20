package com.content_i_like.service;

import com.content_i_like.config.JwtService;
import com.content_i_like.domain.dto.comment.CommentMyFeedResponse;
import com.content_i_like.domain.dto.comment.CommentReadResponse;
import com.content_i_like.domain.dto.follow.FollowMyFeedResponse;
import com.content_i_like.domain.dto.follow.FollowResponse;
import com.content_i_like.domain.dto.member.*;
import com.content_i_like.domain.dto.recommend.RecommendListResponse;
import com.content_i_like.domain.entity.Follow;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.CommentRepository;
import com.content_i_like.repository.FollowRepository;
import com.content_i_like.repository.MemberRepository;
import com.content_i_like.repository.RecommendRepository;
import com.content_i_like.service.validchecks.MemberValidation;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

  private final MemberRepository memberRepository;
  private final RecommendRepository recommendRepository;
  private final CommentRepository commentRepository;
  private final FollowRepository followRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final S3FileUploadService s3FileUploadService;
  private final PointService pointService;
  private final MailService mailService;
  private final MemberValidation memberValidation;

  private final String DEFAULT_PROFILE = "https://content-i-like.s3.ap-northeast-2.amazonaws.com/44b9080f-89c6-4a46-8658-5b3ef1e0bf6d-profile.jpg";

  @Transactional
  public MemberJoinResponse join(MemberJoinRequest memberJoinRequest) {

    //가입한 이력이 있는지 확인 -> 가입 아이디 email 중복 여부 & 사용 중인 닉네임이 아닌지 확인
    memberValidation.validateDuplicatedMember(memberJoinRequest);

    Member member = memberJoinRequest
        .toEntity(passwordEncoder.encode(memberJoinRequest.getPassword()), DEFAULT_PROFILE);

    Member savedMember = memberRepository.save(member);
    pointService.giveWelcomePoint(savedMember);
    return new MemberJoinResponse(savedMember.getMemberNo(), savedMember.getNickName());
  }

  public MemberLoginResponse login(MemberLoginRequest memberLoginRequest) {

    //email 확인
    Member member = memberValidation.validateExistingMemberByEmail(memberLoginRequest.getEmail());

    //password 일치 여부
    if (!passwordEncoder.matches(memberLoginRequest.getPassword(), member.getPassword())) {
      throw new ContentILikeAppException(ErrorCode.INVALID_PASSWORD,
          ErrorCode.INVALID_PASSWORD.getMessage());
    }

    String jwt = jwtService.generateToken(member);
    boolean getPoint = pointService.getAttendancePoint(member);
    if (!getPoint) {
      pointService.giveAttendancePoint(member);
    }
    return new MemberLoginResponse(jwt, member.getMemberNo(), member.getNickName(),
        member.getProfileImgUrl());
  }

  public String findPwByEmail(MemberFindRequest memberFindRequest) {
    Member member = memberValidation.validateExistingMemberByEmail(memberFindRequest.getEmail());

    //name과 일치 여부
    if (!memberFindRequest.getName().equals(member.getName())) {
      throw new ContentILikeAppException(ErrorCode.INCONSISTENT_INFORMATION,
          "잘못된 정보입니다. 이름과 일치하지 않습니다.");
    }

    MailDto mailDto = new MailDto(member.getEmail(), "", "");
    mailService.mailSend(mailDto);

    return "메일로 임시 비밀번호가 전송되었습니다.";
  }

  public String changePw(ChangePwRequest changePwRequest, String memberEmail) {
    Member member = memberValidation.validateExistingMemberByEmail(memberEmail);

    doubleCheckPasswordAndUpdate(member, changePwRequest.getNewPassword(),
        changePwRequest.getVerification());

    return "비밀번호 변경 완료";
  }

  public String changePwByEmail(MemberLoginResponse memberLoginResponse,
      ChangePwRequest changePwRequest) {
    String memberEmail = getEmailByNo(memberLoginResponse);
    return changePw(changePwRequest, memberEmail);
  }

  //같은 비밀번호 2번 입력하여 확인하고 변경하기
  private void doubleCheckPasswordAndUpdate(Member member, String newPassword,
      String verification) {
    if (newPassword.equals(verification)) {
      String password = passwordEncoder.encode(newPassword);
      memberRepository.updateMemberPassword(member.getMemberNo(), password);
    } else {
      throw new ContentILikeAppException(ErrorCode.INCONSISTENT_INFORMATION,
          "비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
    }
  }

  public MemberResponse getMyInfo(String memberEmail) {
    Member member = memberValidation.validateExistingMemberByEmail(memberEmail);
    Long point = pointService.calculatePoint(member);
    MemberResponse memberResponse = MemberResponse.responseWithPoint(member, point);
    return memberResponse;
  }

  public MemberResponse getMyInfoByLoginResponse(MemberLoginResponse loginResponse) {
    String memberEmail = getEmailByNo(loginResponse);
    return getMyInfo(memberEmail);
  }

  public MemberPointResponse getMyPoint(String memberEmail, Pageable pageable) {
    Member member = memberValidation.validateExistingMemberByEmail(memberEmail);
    pageable = PageRequest.of(pageable.getPageNumber(), 20, Sort.by("createdAt").descending());
    List<PointResponse> points = pointService.pointList(member, pageable);
    return new MemberPointResponse(pointService.calculatePoint(member), new PageImpl<>(points));
  }

  public MemberPointResponse getMyPointByLoginResponse(MemberLoginResponse loginResponse, Pageable pageable) {
    String memberEmail = getEmailByNo(loginResponse);
    return getMyPoint(memberEmail, pageable);
  }

  @Transactional
  public MemberResponse modifyMyInfo(MemberModifyRequest memberModifyRequest, MultipartFile file,
      String memberEmail) throws IOException {
    Member member = memberValidation.validateExistingMemberByEmail(memberEmail);

    uploadProfileImg(file, member);
    member.update(memberModifyRequest);

    MemberResponse memberResponse = MemberResponse
        .responseWithPoint(member, pointService.calculatePoint(member));
    return memberResponse;
  }

  @Transactional
  public MemberResponse modifyMyInfoWithFile(MemberModifyRequest request, MemberLoginResponse loginResponse)
      throws IOException {
    String memberEmail = getEmailByNo(loginResponse);
    Member member = memberValidation.validateExistingMemberByEmail(memberEmail);

    String url = getModifyProfileImgURL(request.getProfileImg(), member);
    member.updateProfile(request, url);

    MemberResponse memberResponse = MemberResponse
        .responseWithPoint(member, pointService.calculatePoint(member));
    return memberResponse;
  }

  private String getModifyProfileImgURL(MultipartFile image, Member member) throws IOException {
    String url = member.getProfileImgUrl();
    if (image.isEmpty()) {
      log.info("null: url: {}", url);
      return url;
    }

    String newUrl = s3FileUploadService.uploadFile(image);
    if (!url.equals(DEFAULT_PROFILE)) {
      s3FileUploadService.deleteFile(url.split("/")[3]);
    }
    return newUrl;
  }

  private String uploadProfileImg(MultipartFile file, Member member) throws IOException {
    String url = s3FileUploadService.uploadFile(file);
    member.updateImg(url);
    return url;
  }

  @Transactional
  public String uploadProfileImg(String memberEmail, MultipartFile file) throws IOException {
    Member member = memberValidation.validateExistingMemberByEmail(memberEmail);

    String url = uploadProfileImg(file, member);

    memberRepository.saveAndFlush(member);

    return url;
  }

  public Page<RecommendListResponse> getMyRecommends(String memberEmail, Pageable pageable) {
    Member member = memberValidation.validateExistingMemberByEmail(memberEmail);

    return recommendRepository.findAllByMember(pageable, member).map(RecommendListResponse::of);
  }

  private Long[] getFollowCnt(Member member) {

    Long[] followCnt = new Long[4];

    followCnt[0] = recommendRepository.countByMember(member);   //게시글 수
    followCnt[1] = followRepository.countByMember(member);      //팔로워 수
    followCnt[2] = followRepository.countByFromMemberNo(member.getMemberNo());//팔로윙 수
    followCnt[3] = commentRepository.countByMember(member);

    return followCnt;
  }

  public MemberRecommendResponse getMyRecommendsIntegrated(String memberEmail, Pageable pageable) {
    Member member = memberValidation.validateExistingMemberByEmail(memberEmail);

    Long[] followerCnt = getFollowCnt(member);

    Page<RecommendListResponse> recommendListResponses = recommendRepository.findAllByMember(
        pageable, member)
        .map(RecommendListResponse::of);

    return new MemberRecommendResponse(member, followerCnt, recommendListResponses);
  }

  public MemberCommentResponse getMyComments(String memberEmail,
      Pageable pageable) {
    Member member = memberValidation.validateExistingMemberByEmail(memberEmail);

    Long[] followerCnt = getFollowCnt(member);

    Page<CommentMyFeedResponse> commentMyFeedResponse = commentRepository.findAllByMember(member,
        pageable).map(CommentMyFeedResponse::of);

    return new MemberCommentResponse(member, followerCnt, commentMyFeedResponse);
  }

  public MemberRecommendResponse getMyRecommendsByNickName(String nickName, Pageable pageable) {
    Member member = memberValidation.validateExistingMemberByNickName(nickName);

    Long[] followerCnt = getFollowCnt(member);
    log.info("member={}", member.getMemberNo());

    Page<RecommendListResponse> recommendListResponses = recommendRepository.findAllByMember(
        pageable, member)
        .map(RecommendListResponse::of);

    return new MemberRecommendResponse(member, followerCnt, recommendListResponses);
  }

  public MemberCommentResponse getMyCommentsByNickName(String nickName, Pageable pageable) {
    Member member = memberValidation.validateExistingMemberByNickName(nickName);

    Long[] followerCnt = getFollowCnt(member);

    Page<CommentMyFeedResponse> commentMyFeedResponse = commentRepository.findAllByMember(member,
        pageable).map(CommentMyFeedResponse::of);

    return new MemberCommentResponse(member, followerCnt, commentMyFeedResponse);
  }

  public FollowMyFeedResponse getMyFollowersByNickName(String nickName, Pageable pageable) {
    Member member = memberValidation.validateExistingMemberByNickName(nickName);

    Long[] followerCnt = getFollowCnt(member);

    Stream<FollowResponse> followResponseStream = followRepository.findAllByMember(
        member, pageable).stream()
        .map(follow -> new FollowResponse(
            follow.getFromMemberNo(),
            memberRepository.findById(follow.getFromMemberNo()).get().getNickName(),
            memberRepository.findById(follow.getFromMemberNo()).get().getProfileImgUrl()));

    List<FollowResponse> followResponses = followResponseStream.toList();
    return new FollowMyFeedResponse(member, followerCnt, new PageImpl<>(followResponses));
  }

  public FollowMyFeedResponse getMyFollowingsByNickName(String nickName, Pageable pageable) {
    Member member = memberValidation.validateExistingMemberByNickName(nickName);

    Long[] followerCnt = getFollowCnt(member);

    Stream<FollowResponse> followResponseStream = followRepository.findAllByFromMemberNo(
        member.getMemberNo(), pageable).stream()
        .map(follow -> new FollowResponse(
            follow.getMember().getMemberNo(),
            memberRepository.findById(follow.getMember().getMemberNo()).get().getNickName(),
            memberRepository.findById(follow.getMember().getMemberNo()).get().getProfileImgUrl()));

    List<FollowResponse> followResponses = followResponseStream.toList();
    return new FollowMyFeedResponse(member, followerCnt, new PageImpl<>(followResponses));
  }

  public String getNickNameByNo(Long memberNo) {
    Member member = memberRepository.findById(memberNo)
        .orElseThrow(() -> new ContentILikeAppException(ErrorCode.MEMBER_NOT_FOUND,
            ErrorCode.MEMBER_NOT_FOUND.getMessage()));
    return member.getNickName();
  }

  public String getEmailByNo(MemberLoginResponse response) {
    Optional<Member> member = memberRepository.findById(response.getMemberNo());
    return member.get().getEmail();
  }

  public boolean checkMemberNickName(String nickName) {
    boolean result = false;
    try {
      String chkNickName = memberRepository.findByNickName(nickName).get().getNickName();
      if (chkNickName.equals(nickName)) {
        result = false;
      }
    } catch (Exception e) {
      result = true;
    }
    return result;
  }

  public boolean checkMemberEmail(String email) {
    boolean result = false;
    try {
      String chkEmail = memberRepository.findByEmail(email).get().getNickName();
      if (chkEmail.equals(email)) {
        result = false;
      }
    } catch (Exception e) {
      result = true;
    }
    return result;
  }

  public boolean checkLogin(MemberLoginRequest memberLoginRequest) {
    boolean result = false;
    try {
      Member member = memberRepository.findByEmail(memberLoginRequest.getEmail()).get();
      if (passwordEncoder.matches(memberLoginRequest.getPassword(), member.getPassword())) {
        result = true;
      } else {
        result = false;
      }
    } catch (Exception e) {
      result = false;
    }
    return result;
  }
}
