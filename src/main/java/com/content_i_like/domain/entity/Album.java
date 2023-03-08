package com.content_i_like.domain.entity;

import jakarta.persistence.Column;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="new_album")
public class Album {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="new_album_no")
  private Long albumNo;

  @Column(length = 1000)
  private String albumTitle;

  @Column(length = 1000)
  private String albumImageUrl;

  @Column(length = 5000)
  private String albumSpotifyId;

  @Column(length = 5000)
  private String artistSpotifyId;
  private String releaseDate;
  private String totalTracks;

  @Column(length = 1000)
  private String artistName;

  @ManyToOne
  @JoinColumn(referencedColumnName = "new_artist_no", name = "artist_no")
  private Artist artist;
}
