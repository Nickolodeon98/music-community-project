package com.content_i_like.domain.entity;

import jakarta.persistence.*;

@Entity
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long albumNo;

    public String albumImageUrl;

    @ManyToOne
    @JoinColumn(referencedColumnName = "artistNo", name = "artist_no")
    public Artist artist;
}
