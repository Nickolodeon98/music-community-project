package com.content_i_like.domain.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort.Direction;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SortStrategy {
  private String property;
  private Direction direction;
}
