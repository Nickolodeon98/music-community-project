package com.content_i_like.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Track {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long trackNo;

  public String trackTitle;

  @ManyToOne
  @JoinColumn(referencedColumnName = "albumNo", name = "album_no")
  public Album album;

  @ManyToOne
  @JoinColumn(referencedColumnName = "artistNo", name = "artist_no")
  public Artist artist;
}
