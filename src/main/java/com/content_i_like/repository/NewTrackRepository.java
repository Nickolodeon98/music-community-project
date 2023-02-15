package com.content_i_like.repository;

import com.content_i_like.domain.entity.NewTrack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewTrackRepository extends JpaRepository<NewTrack, Long> {
}
