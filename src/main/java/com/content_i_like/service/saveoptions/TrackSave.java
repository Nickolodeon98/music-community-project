package com.content_i_like.service.saveoptions;

import com.content_i_like.domain.entity.Track;
import com.content_i_like.repository.TrackRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TrackSave implements DBSaveOption<Track> {

  private final TrackRepository trackRepository;
  @Override
  public List<Track> saveNewRowsAllUnique(Set<Track> entities) {
    return trackRepository.saveAll(entities);
  }
}
