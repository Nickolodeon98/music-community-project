package com.content_i_like.repository;

import com.content_i_like.domain.entity.Artist;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
  Optional<Artist> findByArtistName(String artistName);

  Optional<List<Artist>> findAllByArtistSpotifyId(String artistSpotifyId);
}
