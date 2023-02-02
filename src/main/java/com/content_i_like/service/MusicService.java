package com.content_i_like.service;

import static com.content_i_like.service.validchecks.ArbitraryValidationService.validate;

import com.content_i_like.domain.dto.tracks.TrackGetResponse;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Track;
import com.content_i_like.repository.MemberRepository;
import com.content_i_like.repository.TrackRepository;
import com.content_i_like.service.validchecks.MemberValidation;
import com.content_i_like.service.validchecks.TrackValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MusicService {

  private final TrackRepository trackRepository;
  private final MemberRepository memberRepository;

  public TrackGetResponse getASingleTrackInfo(Long trackNo, String memberEmail) {
    Member member = validate(memberEmail, new MemberValidation(memberRepository));

    Track track = validate(String.valueOf(trackNo), new TrackValidation(trackRepository));

    return TrackGetResponse.of(track);
  }
}
