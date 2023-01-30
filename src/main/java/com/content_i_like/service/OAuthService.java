package com.content_i_like.service;

import com.content_i_like.domain.dto.member.UserProfile;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.enums.OAuthAttributes;
import com.content_i_like.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final MemberRepository memberRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2UserService delegate = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);     //구글, 네이버에서 가져온 유저 정보를 담고 있다

    String registrationId = userRequest.getClientRegistration()
        .getRegistrationId();                               //서비스 이름 (ex. google, naver)
    String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
        .getUserInfoEndpoint().getUserNameAttributeName();  //OAuth 로그인 시 키pk가 되는 값
    Map<String, Object> attributes = oAuth2User.getAttributes();//OAuth 서비스의 유저 정보들

    UserProfile userProfile = OAuthAttributes
        .extract(registrationId, attributes);  //registrationId에 따라 유저 정보를 통해 공통된 UserProfile 객체로
    System.out.println(
        "oauthId: " + userProfile.getOauthId() + ", name: " + userProfile.getName() + ", email: "
            + userProfile.getEmail());

    Member member = saveOrUpdate(userProfile);                  //DB 저장
    return new DefaultOAuth2User(
        Collections.singleton(new SimpleGrantedAuthority(String.valueOf(member.getStatus()))),
        attributes,
        userNameAttributeName);
  }

  private Member saveOrUpdate(UserProfile userProfile) {
    Optional<Member> member = memberRepository.findBySnsCheck(userProfile.getOauthId());
    //.map(m->m.update(userProfile.getName(), userProfile.getEmail()))        //OAuth 서비스 사이트에서 정보 변경이 있을 시 DB에 update
    //.orElse(userProfile.toMember());
    if (member
        .isEmpty()) {                                                               //회원가입된 적이 없다면
      Member savedMember = memberRepository.save(userProfile.toMember());             //save
      return savedMember;
    }
    return member.get();
  }
}
