package com.content_i_like.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@jakarta.persistence.Cacheable
@org.hibernate.annotations.Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hashtagNo;
    private String name;

    @org.hibernate.annotations.Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(mappedBy = "hashtag", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostHashtag> postHashtags = new ArrayList();

    public static Hashtag of(String name) {
        return Hashtag.builder()
                .name(name)
                .build();
    }
}
