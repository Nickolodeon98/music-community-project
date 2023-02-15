package com.content_i_like.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class NewAlbum {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long newAlbumNo;

  private String albumTitle;
  private String albumImageUrl;
  private String albumSpotifyId;
  private String releaseDate;
  private String totalTracks;

  private String artistName;

  @ManyToOne
  @JoinColumn(referencedColumnName = "newArtistNo", name = "artist_no")
  private NewArtist artist;
}
