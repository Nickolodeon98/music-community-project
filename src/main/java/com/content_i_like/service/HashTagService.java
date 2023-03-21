package com.content_i_like.service;

import com.content_i_like.domain.dto.recommend.RecommendPostRequest;
import com.content_i_like.domain.entity.Hashtag;
import com.content_i_like.domain.entity.PostHashtag;
import com.content_i_like.domain.entity.Recommend;
import com.content_i_like.repository.HashtagRepository;
import com.content_i_like.repository.PostHashtagRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HashTagService {

  private final HashtagRepository hashtagRepository;
  private final PostHashtagRepository postHashtagRepository;

  public void saveHashTags(RecommendPostRequest request, Recommend post) {
    Optional.ofNullable(request.getHashtag())
        .filter(str -> str.contains("#"))
        .map(str -> Stream.of(str.split("#"))
            .map(h -> h.replaceAll(" ", ""))
            .filter(h -> !h.isEmpty())
            .distinct()
            .toList()).ifPresent(hashtags -> savePostHashtags(hashtags, post));
  }

  public void savePostHashtags(List<String> hashtags, Recommend post) {
    for (String hashtag : hashtags) {
      Hashtag existingHashtag = hashtagRepository.findByName(hashtag)
          .orElseGet(() -> {
            Hashtag newHashtag = Hashtag.of(hashtag);
            hashtagRepository.save(newHashtag);
            return newHashtag;
          });

      PostHashtag postHashtag = PostHashtag.of(post, existingHashtag);
      postHashtagRepository.save(postHashtag);
    }
  }
}
