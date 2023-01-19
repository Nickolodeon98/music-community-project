package com.content_i_like.service;

import com.content_i_like.config.JwtService;
import com.content_i_like.domain.dto.member.MemberJoinRequest;
import com.content_i_like.domain.dto.member.MemberJoinResponse;
import com.content_i_like.domain.dto.member.MemberLoginRequest;
import com.content_i_like.domain.dto.member.MemberLoginResponse;
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
        Member member = validateExistingMember(memberLoginRequest);

        //password 일치 여부
        if(!passwordEncoder.matches(memberLoginRequest.getPassword(), member.getPassword())){
            throw new ContentILikeAppException(ErrorCode.NOT_FOUND,"password가 일치하지 않습니다.");
        }

        String jwt = jwtService.generateToken(member);

        return new MemberLoginResponse(jwt, member.getNickName());
    }

    private Member validateExistingMember(MemberLoginRequest memberLoginRequest) {
        Member member = memberRepository.findByEmail(memberLoginRequest.getEmail())
                .orElseThrow(()-> new ContentILikeAppException(ErrorCode.NOT_FOUND, "존재하지 않는 email입니다."));
        return member;
    }
}
