package com.content_i_like.repository;

import com.content_i_like.domain.entity.NewAlbum;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewAlbumRepository extends JpaRepository<NewAlbum, Long> {
  Optional<List<NewAlbum>> findAllByAlbumSpotifyId(String albumSpotifyId);
}
