package com.content_i_like.service;

import com.content_i_like.config.JwtService;
import com.content_i_like.domain.dto.member.*;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Point;
import com.content_i_like.domain.enums.PointTypeEnum;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.MemberRepository;
import com.content_i_like.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final S3FileUploadService s3FileUploadService;

  @Transactional
  public Member join(MemberJoinRequest memberJoinRequest) {

    //가입한 이력이 있는지 확인 -> 가입 아이디 email 중복 여부 & 사용 중인 닉네임이 아닌지 확인
    validateDuplicatedMember(memberJoinRequest);

    //비밀번호 조건에 맞는지 확인
    if (memberJoinRequest.getPassword().length() < 8
        || memberJoinRequest.getPassword().length() > 16) {
      throw new ContentILikeAppException(ErrorCode.NOT_FOUND, "비밀번호는 8~16자입니다.");
    }

    Member member = memberJoinRequest.toEntity(passwordEncoder.encode(memberJoinRequest.getPassword()));

    Member savedMember = memberRepository.save(member);

    return savedMember;
  }

  private void validateDuplicatedMember(MemberJoinRequest memberJoinRequest) {
    memberRepository.findByEmail(memberJoinRequest.getEmail())
        .ifPresent(member -> {
          throw new ContentILikeAppException(ErrorCode.NOT_FOUND, "이미 가입된 email입니다.");
        });

    memberRepository.findByNickName(memberJoinRequest.getNickName())
        .ifPresent(member -> {
          throw new ContentILikeAppException(ErrorCode.NOT_FOUND, "이미 사용 중인 닉네임입니다.");
        });
  }

  public MemberLoginResponse login(MemberLoginRequest memberLoginRequest) {

    //email 확인
    Member member = validateExistingMember(memberLoginRequest.getEmail());

    //password 일치 여부
    if (!passwordEncoder.matches(memberLoginRequest.getPassword(), member.getPassword())) {
      throw new ContentILikeAppException(ErrorCode.NOT_FOUND, "password가 일치하지 않습니다.");
    }

    String jwt = jwtService.generateToken(member);

    return new MemberLoginResponse(jwt, member.getNickName());
  }

  private Member validateExistingMember(String email) {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new ContentILikeAppException(ErrorCode.NOT_FOUND, "존재하지 않는 email입니다."));
    return member;
  }

  public MailDto findPwByEmail(MemberFindRequest memberFindRequest) {
    Member member = validateExistingMember(memberFindRequest.getEmail());

    //name과 일치 여부
    if (!memberFindRequest.getName().equals(member.getName())) {
      throw new ContentILikeAppException(ErrorCode.NOT_FOUND, "잘못된 정보입니다. 이름과 일치하지 않습니다.");
    }

    return new MailDto(member.getEmail(), "", "");
  }

  public String changePw(ChangePwRequest changePwRequest, String username) {
    Member member = validateExistingMember(username);
    verifyPasswordAndUpdate(member, changePwRequest.getNewPassword(),
        changePwRequest.getVerification());

    return "비밀번호 변경 완료";
  }

  //같은 비밀번호 2번 입력하여 확인하고 변경하기
  private void verifyPasswordAndUpdate(Member member, String newPassword, String verification) {
    if (newPassword.equals(verification)) {
      String password = passwordEncoder.encode(newPassword);
      memberRepository.updateMemberPassword(member.getMemberNo(), password);
    } else {
      throw new ContentILikeAppException(ErrorCode.NOT_FOUND, "비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
    }
  }

  public MemberResponse getMyInfo(String username) {
    Member member = validateExistingMember(username);

    MemberResponse memberResponse = new MemberResponse();
    return memberResponse.toResponse(member);
  }

  @Transactional
  public MemberResponse modifyMyInfo(MemberModifyRequest memberModifyRequest, MultipartFile file,
      String username) throws IOException {
    Member member = validateExistingMember(username);

    uploadProfileImg(file, member);

    verifyPasswordAndUpdate(member, memberModifyRequest.getNewPassword(),
        memberModifyRequest.getVerification());
    member.update(memberModifyRequest);

    MemberResponse memberResponse = new MemberResponse();
    return memberResponse.toResponse(memberRepository.saveAndFlush(member));
  }

  private String uploadProfileImg(MultipartFile file, Member member) throws IOException {
    String url = s3FileUploadService.uploadFile(file);
    member.updateImg(url);
    return url;
  }

  @Transactional
  public String uploadProfileImg(String username, MultipartFile file) throws IOException {
    Member member = validateExistingMember(username);

    String url = uploadProfileImg(file, member);

    memberRepository.saveAndFlush(member);

    return url;
  }
}
