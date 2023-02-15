package com.content_i_like.service.validchecks;

import com.content_i_like.domain.entity.Track;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.TrackRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TrackValidation implements ValidCheck<Track> {
  private final TrackRepository trackRepository;
  @Override
  public Track examine(String toCheck) {
    return trackRepository.findById(Long.parseLong(toCheck))
        .orElseThrow(() -> new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage()));
  }
}
