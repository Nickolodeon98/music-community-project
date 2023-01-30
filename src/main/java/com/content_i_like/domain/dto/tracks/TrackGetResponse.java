package com.content_i_like.domain.dto.tracks;

import com.content_i_like.domain.entity.Song;
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

  public static TrackGetResponse of(Song song) {

    return TrackGetResponse.builder()
        .trackTitle(song.getSongTitle())
        .trackAlbum(song.getAlbum().getAlbumTitle())
        .trackArtist(song.getArtist().getArtistName())
        .build();

  }
}
