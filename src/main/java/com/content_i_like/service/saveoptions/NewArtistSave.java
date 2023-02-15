package com.content_i_like.service.saveoptions;

import com.content_i_like.domain.entity.Artist;
import com.content_i_like.domain.entity.NewArtist;
import com.content_i_like.repository.ArtistRepository;
import com.content_i_like.repository.NewArtistRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NewArtistSave implements NewDBSaveOption<NewArtist> {

  private final NewArtistRepository artistRepository;

  @Override
  public List<NewArtist> saveNewRowsAllUnique(Set<NewArtist> entities) {
    return artistRepository.saveAll(entities);
  }
}
