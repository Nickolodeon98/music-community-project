package com.content_i_like.service;

import com.content_i_like.domain.dto.tracks.TrackGetResponse;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Track;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.MemberRepository;
import com.content_i_like.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MusicService {

  private final TrackRepository trackRepository;
  private final MemberRepository memberRepository;

  interface ValidCheck<T> {
    T examine(String toCheck);
  }

  class MemberValidation implements ValidCheck<Member> {
    @Override
    public Member examine(String toCheck) {
      return memberRepository.findByEmail(toCheck)
              .orElseThrow(() -> new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage()));
    }
  }

  public <T> T validate(String toCheck, ValidCheck<T> validCheck) {
    return validCheck.examine(toCheck);
  }

  public Page<TrackGetResponse> getEveryTrack(Pageable pageable, String memberEmail) {
    Member member = validate(memberEmail, new MemberValidation());

    Page<Track> tracks = trackRepository.findAll(pageable);

    return tracks.map
            (
            trackPage -> TrackGetResponse
                    .of(trackPage, "총 " + tracks.getTotalElements() + "개의 음원을 찾았습니다.")
            );
  }

  public Page<TrackGetResponse> findTracksWithKeyword(Pageable pageable, String searchKey, String memberEmail) {
    TrackGetResponse defaultResponse = TrackGetResponse.builder()
            .message("찾는 음원이 존재하지 않습니다.")
            .build();

    Optional<Page<Track>> tracks = trackRepository.findAllByTrackTitleContaining(searchKey, pageable);

    return tracks.map(
            trackPage -> trackPage
                    .map(entities -> TrackGetResponse
                            .of(entities, "총 " + trackPage.getTotalElements() + "개의 음원을 찾았습니다."))
            )
            .orElseGet(() -> new PageImpl<>(List.of(defaultResponse)));

  }




}