package com.content_i_like.service;

import com.content_i_like.domain.entity.Artist;
import com.content_i_like.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ArtistSave implements DBSaveOption<Artist> {
    private final ArtistRepository artistRepository;

    @Override
    public Artist buildEntity(String title) {
        return Artist.builder().artistName(title).build();
    }

    @Override
    public void saveNewRow(Artist artist) {
        artistRepository.save(artist);
    }
}
