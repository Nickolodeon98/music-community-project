package com.content_i_like.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long albumNo;
    private String albumTitle;

    private String albumImageUrl;

    @ManyToOne
    @JoinColumn(referencedColumnName = "artistName", name = "artist_name")
    private Artist artist;
}
