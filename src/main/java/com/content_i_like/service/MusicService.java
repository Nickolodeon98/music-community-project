package com.content_i_like.service;

import com.content_i_like.domain.dto.tracks.TrackGetResponse;
import com.content_i_like.domain.entity.Song;
import com.content_i_like.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MusicService {

    private final SongRepository songRepository;

    public Page<TrackGetResponse> getEveryTrack(Pageable pageable) {
        Page<Song> tracks = songRepository.findAll(pageable);

        return tracks.map(TrackGetResponse::of);
    }
}
