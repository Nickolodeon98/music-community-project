package com.content_i_like.service;

import com.content_i_like.domain.entity.Album;
import com.content_i_like.domain.entity.Artist;
import com.content_i_like.domain.entity.Song;
import com.content_i_like.repository.SongRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TrackSave implements DBSaveOption<Song> {
    private final SongRepository songRepository;

    @Override
    public Song saveNewRow(Song song) {
        return songRepository.save(song);
    }

    @Override
    public List<Song> saveNewRows(List<Song> entities) {
        return songRepository.saveAll(entities);
    }

    @Override
    public List<Song> fetchEverything() {
        return songRepository.findAll();
    }
}
