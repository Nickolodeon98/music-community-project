package com.content_i_like.service;

import com.content_i_like.domain.entity.Song;
import com.content_i_like.repository.SongRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TrackSave implements DBSaveOption<Song> {
    private final SongRepository songRepository;
    @Override
    public Song buildEntity(String title) {
        return Song.builder().build();
    }

    @Override
    public Song saveNewRow(Song song) {
        return songRepository.save(song);
    }
}
