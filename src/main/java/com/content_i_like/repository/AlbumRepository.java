package com.content_i_like.repository;

import com.content_i_like.domain.entity.Album;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {

  List<Album> findAllByAlbumTitleContaining(String keyword);
}
