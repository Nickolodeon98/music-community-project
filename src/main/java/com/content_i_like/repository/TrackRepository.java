package com.content_i_like.repository;


import com.content_i_like.domain.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Long> {

}
