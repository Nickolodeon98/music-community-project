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
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "new_track")
@jakarta.persistence.Cacheable
@org.hibernate.annotations.Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class Track {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="new_track_no")
  private Long trackNo;

  @Column(length = 2000)
  private String trackTitle;

  @Column(length = 2000)
  private String artistName;

  @Column(length = 2000)
  private String albumSpotifyId;

  @Column(length = 5000)
  private String artistSpotifyId;
  private String trackReleaseDate;
  private String albumTotalTracks;


  @ManyToOne
  @JoinColumn(referencedColumnName = "new_album_no", name = "album_no")
  public Album album;

  @ManyToOne
  @JoinColumn(referencedColumnName = "new_artist_no", name = "artist_no")
  public Artist artist;
}
