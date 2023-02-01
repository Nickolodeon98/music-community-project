package com.content_i_like.repository;


import com.content_i_like.domain.entity.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrackRepository extends JpaRepository<Track, Long> {

    Optional<Page<Track>> findAllBytrackTitleContaining(String searchKey, Pageable pageable);
}
