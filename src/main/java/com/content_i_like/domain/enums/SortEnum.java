package com.content_i_like.domain.enums;

import lombok.Getter;
import org.springframework.data.domain.Sort.Direction;

@Getter
public enum SortEnum {
  TRACKS_SORT_DEFAULT("trackNo", 8, Direction.ASC),
  RECOMMENDS_SORT_DEFAULT("recommendViews", 5, Direction.DESC),
  MEMBERS_SORT_DEFAULT("memberNo", 10, Direction.ASC);

  final String sortBy;
  final int scale;
  final Direction direction;
  SortEnum(String sortBy, int scale, Direction direction) {
    this.sortBy = sortBy;
    this.scale = scale;
    this.direction = direction;
  }
}
