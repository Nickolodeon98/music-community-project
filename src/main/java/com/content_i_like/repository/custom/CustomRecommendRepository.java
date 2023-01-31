package com.content_i_like.repository.custom;

import com.content_i_like.domain.entity.Recommend;
import java.util.List;

public interface CustomRecommendRepository {

  List<Recommend> getListBySort(String sort);

}
