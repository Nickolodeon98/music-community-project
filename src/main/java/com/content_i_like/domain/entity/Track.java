package com.content_i_like.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "new_track")
public class Track {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long trackNo;

  private String trackTitle;
  private String artistName;
  private String albumSpotifyId;
  private String artistSpotifyId;
  private String trackReleaseDate;
  private String albumTotalTracks;

  @ManyToOne
  @JoinColumn(referencedColumnName = "albumNo", name = "album_no")
  public Album album;

  @ManyToOne
  @JoinColumn(referencedColumnName = "artistNo", name = "artist_no")
  public Artist artist;
}
