package com.content_i_like.service.searchtools;

import com.content_i_like.domain.dto.tracks.TrackGetResponse;
import com.content_i_like.repository.TrackRepository;
import com.content_i_like.service.TrackService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class NewTracksSearchTool extends TracksSearchTool {
  private final TrackService trackService;

  public NewTracksSearchTool(TrackRepository trackRepository, TrackService trackService) {
    super(trackRepository);
    this.trackService = trackService;
  }

  @Override
  public Page<TrackGetResponse> search(String keyword, Pageable pageable) {
    Page<TrackGetResponse> foundTracks = super.search(keyword, pageable);

    if (foundTracks.isEmpty())
      foundTracks = trackService.searchFromApi();

    return foundTracks;
  }

}
