package com.content_i_like.domain.dto.tracks;

import lombok.*;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TrackPageGetResponse {

  private String message;
  private Page<TrackGetResponse> tracks;

  public static TrackPageGetResponse of(Page<TrackGetResponse> pagedTracks, String message) {
    return TrackPageGetResponse.builder()
        .message(message)
        .tracks(pagedTracks)
        .build();
  }
}
