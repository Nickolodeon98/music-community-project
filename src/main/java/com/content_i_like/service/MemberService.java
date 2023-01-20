package com.content_i_like.service;

import com.content_i_like.config.JwtService;
import com.content_i_like.domain.dto.member.*;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public MemberJoinResponse join(MemberJoinRequest memberJoinRequest){

        //가입한 이력이 있는지 확인 -> 가입 아이디 email
        validateDuplicatedMember(memberJoinRequest);

        //비밀번호 조건에 맞는지 확인
        if(memberJoinRequest.getPassword().length()<8 || memberJoinRequest.getPassword().length()>16){
            throw new ContentILikeAppException(ErrorCode.NOT_FOUND, "비밀번호는 8~16자입니다.");
        }

        Member savedMember = memberRepository.save(memberJoinRequest.toEntity(passwordEncoder.encode(memberJoinRequest.getPassword())));

        return new MemberJoinResponse(savedMember.getMemberNo(), savedMember.getNickName());
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

    public MemberLoginResponse login(MemberLoginRequest memberLoginRequest){

        //email 확인
        Member member = validateExistingMember(memberLoginRequest.getEmail());

        //password 일치 여부
        if(!passwordEncoder.matches(memberLoginRequest.getPassword(), member.getPassword())){
            throw new ContentILikeAppException(ErrorCode.NOT_FOUND,"password가 일치하지 않습니다.");
        }

        String jwt = jwtService.generateToken(member);

        return new MemberLoginResponse(jwt, member.getNickName());
    }

    private Member validateExistingMember(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new ContentILikeAppException(ErrorCode.NOT_FOUND, "존재하지 않는 email입니다."));
        return member;
    }

    public MailDto findPwByEmail(MemberFindRequest memberFindRequest) {
        Member member = validateExistingMember(memberFindRequest.getEmail());

        //name과 일치 여부
        if(!memberFindRequest.getName().equals(member.getName())){
            throw new ContentILikeAppException(ErrorCode.NOT_FOUND,"잘못된 정보입니다. 이름과 일치하지 않습니다.");
        }

        return new MailDto(member.getEmail(),"","");
    }

    public String changePw(ChangePwRequest changePwRequest, String username) {
        Member member = validateExistingMember(username);

        //같은 비밀번호 2번 입력하여 확인하기
        if(changePwRequest.getNewPassword().equals(changePwRequest.getVerification())){
            memberRepository.updateMemberPassword(member.getMemberNo(), changePwRequest.getNewPassword());
        } else {
            throw new ContentILikeAppException(ErrorCode.NOT_FOUND,"비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
        }

        return "비밀번호 변경 완료";
    }
}
