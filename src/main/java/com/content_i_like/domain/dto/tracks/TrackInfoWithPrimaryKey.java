package com.content_i_like.domain.dto.tracks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrackInfoWithPrimaryKey {
  private Long pk;
}
