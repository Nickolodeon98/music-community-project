package com.content_i_like.repository;

import com.content_i_like.domain.entity.Album;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {

  Optional<Page<Album>> findAllByAlbumTitleContaining(String keyword, Pageable pageable);
}
