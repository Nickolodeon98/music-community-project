package com.content_i_like.service.saveoptions;

import com.content_i_like.domain.entity.NewTrack;
import com.content_i_like.domain.entity.Track;
import com.content_i_like.repository.NewTrackRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NewTrackSave implements NewDBSaveOption<NewTrack> {

  private final NewTrackRepository trackRepository;
  @Override
  public List<NewTrack> saveNewRowsAllUnique(Set<NewTrack> entities) {
    return trackRepository.saveAll(entities);
  }
}
