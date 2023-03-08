package com.content_i_like.service.saveoptions;

import com.content_i_like.domain.entity.Artist;
import com.content_i_like.repository.ArtistRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ArtistSave implements DBSaveOption<Artist> {

  private final ArtistRepository artistRepository;

  @Override
  public List<Artist> saveNewRowsAllUnique(Set<Artist> entities) {
    return artistRepository.saveAll(entities);
  }
}
