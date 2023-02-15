package com.content_i_like.service.saveoptions;

import com.content_i_like.domain.entity.Album;
import com.content_i_like.repository.AlbumRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AlbumSave implements DBSaveOption<Album> {

  private final AlbumRepository albumRepository;
  @Override
  public List<Album> saveNewRowsAllUnique(Set<Album> entities) {
    return albumRepository.saveAll(entities);
  }
}
