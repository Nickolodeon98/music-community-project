package com.content_i_like.domain.entity;

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
import org.springframework.context.annotation.EnableMBeanExport;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class NewTrack {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long newTrackNo;

  private String trackTitle;
  private String artistName;
  private String albumSpotifyId;
  private String artistSpotifyId;
  private String trackReleaseDate;
  private String albumTotalTracks;

  @ManyToOne
  @JoinColumn(referencedColumnName = "newAlbumNo", name = "album_no")
  public NewAlbum album;

  @ManyToOne
  @JoinColumn(referencedColumnName = "newArtistNo", name = "artist_no")
  public NewArtist artist;
}
