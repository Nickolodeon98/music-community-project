package com.content_i_like.repository;

import com.content_i_like.domain.entity.Album;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {
  Optional<List<Album>> findAllByAlbumSpotifyId(String albumSpotifyId);

  List<Album> findAllByAlbumTitleContaining(String keyword);
}
