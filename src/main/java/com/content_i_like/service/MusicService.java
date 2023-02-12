package com.content_i_like.service;

import static com.content_i_like.service.validchecks.ArbitraryValidationService.validate;

import com.content_i_like.domain.dto.recommend.RecommendReadResponse;
import com.content_i_like.domain.dto.search.SearchRecommendsResponse;
import com.content_i_like.domain.dto.tracks.TrackGetResponse;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Recommend;
import com.content_i_like.domain.entity.Track;
import com.content_i_like.exception.ContentILikeAppException;
import com.content_i_like.exception.ErrorCode;
import com.content_i_like.repository.MemberRepository;
import com.content_i_like.repository.RecommendRepository;
import com.content_i_like.repository.TrackRepository;
import com.content_i_like.service.validchecks.MemberValidation;
import com.content_i_like.service.validchecks.TrackValidation;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MusicService {

  private final TrackRepository trackRepository;
  private final MemberRepository memberRepository;
  private final RecommendRepository recommendRepository;

  public TrackGetResponse getASingleTrackInfo(Long trackNo, Pageable pageable, String memberEmail) {
    Member member = validate(memberEmail, new MemberValidation(memberRepository));

    Track track = validate(String.valueOf(trackNo), new TrackValidation(trackRepository));

    Page<SearchRecommendsResponse> recommends = getEveryRecommendByTrack(track, pageable);

    return TrackGetResponse.of(track, recommends);
  }

  private Page<SearchRecommendsResponse> getEveryRecommendByTrack(Track track, Pageable pageable) {
    Page<Recommend> recommends = recommendRepository.findAllByTrack(track, pageable)
        .orElseThrow(()->new ContentILikeAppException(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage()));

    return recommends.map(recommend ->
        recommend.getRecommendContent().length() < 50
            ? SearchRecommendsResponse.of(recommend, recommend.getRecommendContent())
            : SearchRecommendsResponse.of(recommend, recommend.getRecommendContent().substring(0, 50)));
  }
}
