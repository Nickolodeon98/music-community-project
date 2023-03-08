package com.content_i_like.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@jakarta.persistence.Cacheable
@org.hibernate.annotations.Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class PostHashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postHashtagNo;

    @ManyToOne
    @JoinColumn(name = "recommend_no")
    private Recommend recommend;

    @ManyToOne
    @JoinColumn(name = "hashtag_no")
    private Hashtag hashtag;

    public static PostHashtag of(Recommend recommend, Hashtag existingHashtag) {
        return PostHashtag.builder()
                .recommend(recommend)
                .hashtag(existingHashtag)
                .build();
    }
}
