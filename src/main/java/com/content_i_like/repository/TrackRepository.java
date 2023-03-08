package com.content_i_like.repository;

import static org.hibernate.annotations.QueryHints.CACHEABLE;

import com.content_i_like.domain.entity.Track;
import jakarta.persistence.QueryHint;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

public interface TrackRepository extends JpaRepository<Track, Long> {
  @QueryHints(@QueryHint(name=CACHEABLE, value="true"))
  Optional<Track> findByTrackTitle(String trackTitle);

  @QueryHints(@QueryHint(name=CACHEABLE, value="true"))
  Optional<Page<Track>> findAllByTrackTitleContainingOrAlbumAlbumTitleContainingOrArtistArtistNameContaining
      (String trackTitle, String albumTitle, String artistName, Pageable pageable);
}
