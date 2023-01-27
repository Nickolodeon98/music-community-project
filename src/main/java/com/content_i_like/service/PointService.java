package com.content_i_like.service;

import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Point;
import com.content_i_like.domain.enums.PointTypeEnum;
import com.content_i_like.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService {

  private final PointRepository pointRepository;

  @Transactional
  public void giveWelcomePoint(Member member) {
    Point point = Point.builder()
        .pointType(PointTypeEnum.WELCOME_POINT)
        .member(member)
        .pointExpense(0l)
        .pointIncome(1000l)
        .build();

    pointRepository.save(point);
  }
}
