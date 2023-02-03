package com.content_i_like.domain.dto.tracks;

import com.content_i_like.domain.entity.Track;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TrackGetResponse {

  private String trackTitle;
  private String trackAlbum;
  private String trackArtist;
  private String thumbnailUrl;

  public static TrackGetResponse of(Track track) {
    return TrackGetResponse.builder()
        .trackTitle(track.getTrackTitle())
        .trackAlbum(track.getAlbum().getAlbumTitle())
        .trackArtist(track.getArtist().getArtistName())
        .thumbnailUrl(track.getAlbum().getAlbumImageUrl())
        .build();
  }

  public Track toEntity() {
    return Track.builder().trackTitle(this.trackTitle).build();
  }
}
