package com.content_i_like.service;

import com.content_i_like.domain.dto.member.MemberJoinRequest;
import com.content_i_like.domain.dto.member.MemberJoinResponse;
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

    public MemberJoinResponse join(MemberJoinRequest memberJoinRequest){

        //가입한 이력이 있는지 확인 -> 가입 아이디 email
        memberRepository.findByEmail(memberJoinRequest.getEmail())
                .ifPresent(member -> {
                    throw new ContentILikeAppException(ErrorCode.NOT_FOUND, "이미 가입된 email입니다.");
                });

        //이미 사용 중인 닉네임인지 확인
        memberRepository.findByNickName(memberJoinRequest.getNickName())
                .ifPresent(member -> {
                    throw new ContentILikeAppException(ErrorCode.NOT_FOUND, "이미 사용 중인 닉네임입니다.");
                });

        Member savedMember = memberRepository.save(memberJoinRequest.toEntity(passwordEncoder.encode(memberJoinRequest.getPassword())));

        return new MemberJoinResponse(savedMember.getMemberNo(), savedMember.getNickName());
    }
}
