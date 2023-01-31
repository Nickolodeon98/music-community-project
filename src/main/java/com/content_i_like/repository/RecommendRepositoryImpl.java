package com.content_i_like.repository;

import static com.content_i_like.domain.entity.QRecommend.recommend;

import com.content_i_like.domain.entity.Recommend;
import com.content_i_like.repository.custom.CustomRecommendRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;

public class RecommendRepositoryImpl implements CustomRecommendRepository {

  private final JPAQueryFactory queryFactory;

  public RecommendRepositoryImpl(EntityManager entityManager) {
    this.queryFactory = new JPAQueryFactory(entityManager);
  }

  @Override
  public List<Recommend> getListBySort(String sort) {
    return queryFactory.select(recommend)
        .from(recommend)
        .fetch();
  }
}
