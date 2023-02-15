package com.content_i_like.service.saveoptions;

import com.content_i_like.domain.entity.Album;
import com.content_i_like.domain.entity.NewAlbum;
import com.content_i_like.repository.NewAlbumRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NewAlbumSave implements NewDBSaveOption<NewAlbum> {

  private final NewAlbumRepository albumRepository;
  @Override
  public List<NewAlbum> saveNewRowsAllUnique(Set<NewAlbum> entities) {
    return albumRepository.saveAll(entities);
  }
}
