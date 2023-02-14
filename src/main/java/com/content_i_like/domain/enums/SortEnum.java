package com.content_i_like.domain.enums;

import lombok.Getter;
import org.springframework.data.domain.Sort.Direction;

@Getter
public enum SortEnum {
  TRACKS_SORT_DEFAULT("trackTitle", 8, Direction.ASC),
  RECOMMENDS_SORT_DEFAULT("createdAt", 5, Direction.DESC),
  MEMBERS_SORT_DEFAULT("createdAt", 10, Direction.DESC);

  final String sortBy;
  final int scale;
  final Direction direction;
  SortEnum(String sortBy, int scale, Direction direction) {
    this.sortBy = sortBy;
    this.scale = scale;
    this.direction = direction;
  }
}
