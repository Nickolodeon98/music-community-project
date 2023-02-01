package com.content_i_like.service;

import com.content_i_like.domain.entity.Album;
import com.content_i_like.domain.entity.Artist;
import com.content_i_like.domain.entity.Track;
import com.content_i_like.repository.TrackRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TrackSave implements DBSaveOption<Track> {
    private final TrackRepository trackRepository;

    @Override
    public Track saveNewRow(Track track) {
        return trackRepository.save(track);
    }

    @Override
    public List<Track> saveNewRows(List<Track> entities) {
//        for (Track entity : entities) {
//            trackRepository.findByTrackTitle(entity.getTrackTitle())
//                    .ifPresent(args->entities.remove(entity));
            /* 문제는 음원 이름만 같고 앨범명이나 아티스트명이 다른 음원들이다.
             * 음원의 개수는 줄어들었는데 앨범명과 아티스트명의 개수가 그대로이면
             * 연관관계를 설정할 때 참조할 수 있는 개수가 맞지 않는다. */
//        }
        return trackRepository.saveAll(entities);
    }

    @Override
    public List<Track> fetchEverything() {
        return trackRepository.findAll();
    }
}
