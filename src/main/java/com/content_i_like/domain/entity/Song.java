package com.content_i_like.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.annotation.processing.Generated;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long songNo;

    public String songTitle;

    @ManyToOne
    @JoinColumn(referencedColumnName = "albumName", name = "album_name")
    public Album album;

    @ManyToOne
    @JoinColumn(referencedColumnName = "artistName", name="artist_name")
    public Artist artist;
}
