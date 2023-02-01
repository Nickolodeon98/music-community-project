package com.content_i_like.service;

import com.content_i_like.domain.dto.tracks.TrackGetResponse;
import com.content_i_like.domain.dto.tracks.TrackPageGetResponse;
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

import java.util.Collections;
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

    return tracks.map(TrackGetResponse::of);
  }

  public TrackPageGetResponse findTracksWithKeyword(Pageable pageable, String searchKey, String memberEmail) {
    Optional<Page<Track>> tracks = trackRepository.findAllByTrackTitleContaining(searchKey, pageable);

    Page<TrackGetResponse> pagedTracks =
            tracks.map(trackPage -> trackPage.map(TrackGetResponse::of))
            .orElseGet(() -> new PageImpl<>(Collections.emptyList()));

    return pagedTracks.isEmpty()
            ? TrackPageGetResponse.of(pagedTracks, "찾는 음원이 존재하지 않습니다.")
            : TrackPageGetResponse.of(pagedTracks, "총 " + pagedTracks.getTotalElements() + "개의 음원을 찾았습니다.");
  }




}