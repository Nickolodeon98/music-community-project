package com.content_i_like.repository;

import static com.content_i_like.domain.entity.QComment.comment;
import static com.content_i_like.domain.entity.QRecommend.recommend;


import com.content_i_like.domain.dto.chart.RecommendChartResponse;
import com.content_i_like.domain.dto.chart.TrackChartResponse;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ChartQueryRepository {

  private final JPAQueryFactory queryFactory;

  public ChartQueryRepository(JPAQueryFactory jpaQueryFactory) {
    this.queryFactory = jpaQueryFactory;
  }


  /**
   * 추천글 차트 (최대 10개)
   *
   * @param
   * @return Tuple 순위, 추천글 제목, 작성자, 포인트, 조회수
   */

  public List<RecommendChartResponse> getMonthlyRecommendChartTop10() throws Exception {
    return getRecommendChart(4, 10);
  }

  public List<RecommendChartResponse> getWeeklyRecommendChartTop10() throws Exception {
    return getRecommendChart(1, 10);
  }

  public List<TrackChartResponse> getMonthlyTrackChartTop10() throws Exception {
    return getTrackChart(4, 10);
  }

  public List<TrackChartResponse> getWeeklyTrackChartTop10() throws Exception {
    return getTrackChart(1, 10);
  }
  List<RecommendChartResponse> getRecommendChart(Integer expirationDateInWeeks, Integer limitSize)
      throws Exception {

    // 추천글 유효기간
    LocalDateTime validUntilThisTime = LocalDateTime.now().minusWeeks(expirationDateInWeeks);

    // 별칭 지정
    StringPath totalScore = Expressions.stringPath("totalScore");

    /*
     * 추천글 left join 댓글
     * 추천글 번호, 추천글 제목, 추천글 작성자 이름, 추천글 총합 점수("score"), 추천글 조회수
     * score = 추천글 포인트 + 좋아요 갯수 + 댓글 갯수 + 댓글 포인트 총합
     * 정렬 기준: 1. score 2. 조회수 3. 제목 이름순 4. 추천글 번호순 (오름차순)
     */

    List<Tuple> findChart = queryFactory
        .select(
            recommend.recommendNo,
            recommend.recommendTitle,
            recommend.member.memberNo,
            recommend.member.nickName,
            (recommend.recommendPoint.coalesce(0L).add(recommend.likes.size())
                .add(recommend.comments.size()).add(comment.commentPoint.coalesce(0L).sum())).as(
                "totalScore"),
            recommend.recommendViews)
        .from(recommend)
        .leftJoin(recommend.comments, comment)
        .on(recommend.recommendNo.eq(comment.recommend.recommendNo))
        .where(recommend.createdAt.after(validUntilThisTime))
        .groupBy(recommend.recommendNo)
        .orderBy(totalScore.desc(), recommend.recommendViews.desc(), recommend.recommendTitle.asc(),
            recommend.recommendNo.asc())
        .limit(limitSize)
        .fetch();

    // response 로 변환
    List<RecommendChartResponse> responses = new ArrayList<>();
    for (Tuple tuple : findChart) {
      Long recommendNo = Long.valueOf(String.valueOf(tuple.get(recommend.recommendNo)));
      String recommendTitle = String.valueOf(tuple.get(recommend.recommendTitle));
      Long memberNo = Long.valueOf(String.valueOf(tuple.get(recommend.member.memberNo)));
      String memberNickName = String.valueOf(tuple.get(recommend.member.nickName));
      Long recommendScore = Long.valueOf(String.valueOf(tuple.get(totalScore)));
      Long recommendViews = Long.valueOf(String.valueOf(tuple.get(recommend.recommendViews)));

      responses.add(
          new RecommendChartResponse(recommendNo, recommendTitle, memberNo, memberNickName,
              recommendScore,
              recommendViews));
    }

    return responses;
  }

  List<TrackChartResponse> getTrackChart(Integer expirationDateInWeeks, Integer limitSize)
      throws Exception {
    // 추천글 유효기간
    LocalDateTime validUntilThisTime = LocalDateTime.now().minusWeeks(expirationDateInWeeks);

    // 별칭 지정
    StringPath totalScore = Expressions.stringPath("totalScore");

    List<Tuple> findChart = queryFactory
        .select(
            recommend.track.trackNo,
            recommend.track.trackTitle,
            recommend.track.album.albumNo,
            recommend.track.album.albumImageUrl,
            recommend.track.artist.artistNo,
            recommend.track.artist.artistName,
            (recommend.recommendPoint
                .add(comment.commentPoint.coalesce(0L).sum())
                .add(recommend.comments.size())
                .add(recommend.likes.size())).as("totalScore")
        )
        .from(recommend)
        .leftJoin(recommend.comments, comment)
        .on(recommend.recommendNo.eq(comment.recommend.recommendNo))
        .where(recommend.createdAt.after(validUntilThisTime))
        .groupBy(recommend.track.trackNo)
        .orderBy(totalScore.desc(), recommend.track.trackTitle.asc(),
            recommend.track.artist.artistName.asc())
        .limit(limitSize)
        .fetch();

    // response 로 변환
    List<TrackChartResponse> responses = new ArrayList<>();
    for (Tuple tuple : findChart) {
      Long trackNo = Long.valueOf(String.valueOf(tuple.get(recommend.track.trackNo)));
      String trackTitle = String.valueOf(tuple.get(recommend.track.trackTitle));
      Long albumNo = Long.valueOf(String.valueOf(tuple.get(recommend.track.album.albumNo)));
      String albumImageUrl = String.valueOf(tuple.get(recommend.track.album.albumImageUrl));
      Long artistNo = Long.valueOf(String.valueOf(tuple.get(recommend.track.artist.artistNo)));
      String artistName = String.valueOf(tuple.get(recommend.track.artist.artistName));
      Long trackScore = Long.valueOf(String.valueOf(tuple.get(totalScore)));

      responses.add(new TrackChartResponse(trackNo, trackTitle, albumNo, albumImageUrl, artistNo, artistName,
          trackScore));
    }

    return responses;
  }

}
