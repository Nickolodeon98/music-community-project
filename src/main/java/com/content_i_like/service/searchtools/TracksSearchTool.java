package com.content_i_like.service.searchtools;

import com.content_i_like.domain.dto.tracks.TrackGetResponse;
import com.content_i_like.repository.TrackRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class TracksSearchTool implements ItemSearch<TrackGetResponse> {

  private final TrackRepository trackRepository;

  @Override
  public Page<TrackGetResponse> searchAll(Pageable pageable) {
    return trackRepository.findAll(pageable).map(TrackGetResponse::of);
  }

  @Override
  public Page<TrackGetResponse> search(String keyword, Pageable pageable) {
    return trackRepository
        .findAllByTrackTitleContainingOrAlbumAlbumTitleContainingOrArtistArtistNameContaining
            (keyword, keyword, keyword, pageable)
        .map(trackPage -> trackPage.map(TrackGetResponse::of))
        .orElseGet(() -> new PageImpl<>(Collections.emptyList()));
  }

  @Override
  public String buildMessage() {
    return "음원을";
  }
}
