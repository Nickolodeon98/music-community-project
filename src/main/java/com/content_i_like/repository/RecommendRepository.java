package com.content_i_like.repository;

import com.content_i_like.domain.entity.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {
    @Override
    @Modifying
    <S extends Recommend> S save(S entity);
}
