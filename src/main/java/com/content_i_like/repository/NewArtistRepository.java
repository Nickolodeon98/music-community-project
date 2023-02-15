package com.content_i_like.repository;

import com.content_i_like.domain.entity.Artist;
import com.content_i_like.domain.entity.NewArtist;
import com.content_i_like.service.saveoptions.NewArtistSave;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewArtistRepository extends JpaRepository<NewArtist, Long> {
  Optional<NewArtist> findByArtistName(String artistName);

  Optional<List<NewArtist>> findAllByArtistSpotifyId(String artistSpotifyId);
}
