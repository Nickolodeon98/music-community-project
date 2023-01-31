package com.content_i_like.repository;


import com.content_i_like.domain.entity.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long> {

    Optional<Page<Song>> findAllBySongTitleContaining(String searchKey, Pageable pageable);
}
