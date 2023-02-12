package com.content_i_like.domain.dto.tracks;

import com.content_i_like.domain.dto.search.SearchRecommendsResponse;
import com.content_i_like.domain.entity.Track;
import java.util.List;
import lombok.*;
import org.springframework.data.domain.Page;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TrackGetResponse {

  private Long trackNo;
  private String trackTitle;
  private String trackAlbum;
  private String trackArtist;
  private String thumbnailUrl;
  private Page<SearchRecommendsResponse> recommendsOfTracks;

  public static TrackGetResponse of(Track track, Page<SearchRecommendsResponse> recommendsOfTracks) {
    return TrackGetResponse.builder()
        .trackNo(track.getTrackNo())
        .trackTitle(track.getTrackTitle())
        .trackAlbum(track.getAlbum().getAlbumTitle())
        .trackArtist(track.getArtist().getArtistName())
        .thumbnailUrl(track.getAlbum().getAlbumImageUrl())
        .recommendsOfTracks(recommendsOfTracks)
        .build();
  }

  public static TrackGetResponse of(Track track) {
    return TrackGetResponse.builder()
        .trackNo(track.getTrackNo())
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
