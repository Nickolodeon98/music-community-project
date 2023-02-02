package com.content_i_like.service;

import com.content_i_like.domain.dto.tracks.TrackGetResponse;
import com.content_i_like.domain.entity.Track;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MusicService {

  private final TrackRepository trackRepository;

  public TrackGetResponse getASingleTrackInfo(Long trackNo) {
    Track track = trackRepository.findById(trackNo)
        .orElseThrow(()->new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage()));

    return TrackGetResponse.of(track);
  }
}
