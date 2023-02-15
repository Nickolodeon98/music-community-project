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
  private Long albumNo;

  private String albumTitle;
  private String albumImageUrl;
  private String albumSpotifyId;

  @Column(length = 5000)
  private String artistSpotifyId;
  private String releaseDate;
  private String totalTracks;

  private String artistName;

  @ManyToOne
  @JoinColumn(referencedColumnName = "artistNo", name = "artist_no")
  private Artist artist;
}
