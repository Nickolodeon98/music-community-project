package com.content_i_like.service;

import com.content_i_like.domain.entity.Album;
import com.content_i_like.repository.AlbumRepository;
import com.content_i_like.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AlbumSave implements DBSaveOption<Album> {

    private final AlbumRepository albumRepository;

    @Override
    public Album buildEntity(String title) {
        return Album.builder().albumTitle(title).build();
    }

    @Override
    public Album saveNewRow(Album album) {
        return albumRepository.save(album);
    }
}
