package com.content_i_like.domain.dto.tracks;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TrackResponse {

  private List<String> titles;
}
