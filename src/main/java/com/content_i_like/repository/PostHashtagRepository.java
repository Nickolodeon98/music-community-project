package com.content_i_like.repository;

import com.content_i_like.domain.entity.PostHashtag;
import com.content_i_like.domain.entity.Recommend;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long> {

  List<PostHashtag> findAllByRecommendRecommendNo(Long recommendNo);

  void deleteAllByRecommend(Recommend recommend);
}
