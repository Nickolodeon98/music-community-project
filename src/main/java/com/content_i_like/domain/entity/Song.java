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
    @JoinColumn(referencedColumnName = "albumNo", name = "album_no")
    public Album album;

    @ManyToOne
    public Artist artist;
}
