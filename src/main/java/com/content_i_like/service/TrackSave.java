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
//        for (Song entity : entities) {
//            songRepository.findBySongTitle(entity.getSongTitle())
//                    .ifPresent(args->entities.remove(entity));
            /* 문제는 음원 이름만 같고 앨범명이나 아티스트명이 다른 음원들이다.
             * 음원의 개수는 줄어들었는데 앨범명과 아티스트명의 개수가 그대로이면
             * 연관관계를 설정할 때 참조할 수 있는 개수가 맞지 않는다. */
//        }
        return songRepository.saveAll(entities);
    }

    @Override
    public List<Song> fetchEverything() {
        return songRepository.findAll();
    }
}
