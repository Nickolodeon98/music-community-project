package com.content_i_like.domain.dto.tracks;

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
}
