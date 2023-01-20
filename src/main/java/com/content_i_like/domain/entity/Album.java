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

    private String albumImageUrl;

    @ManyToOne
    @JoinColumn(referencedColumnName = "artistNo", name = "artist_no")
    private Artist artist;
}
