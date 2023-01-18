package com.content_i_like.domain.entity;

import jakarta.persistence.*;

import javax.annotation.processing.Generated;

@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long songNo;

    public String songTitle;

    @ManyToOne
    @JoinColumn(referencedColumnName = "albumNo", name = "album_no")
    public Album album;
}
