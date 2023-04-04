package com.content_i_like.query;

import static com.content_i_like.domain.entity.QComment.comment;
import static com.content_i_like.domain.entity.QRecommend.recommend;

import com.content_i_like.domain.dto.chart.RecommendChartResponse;
import com.content_i_like.domain.entity.QComment;
import com.content_i_like.domain.entity.QRecommend;
import com.content_i_like.domain.entity.QTrack;
import com.content_i_like.service.RecommendService;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ChartQueryRepositoryTest {

  @MockBean
  RecommendService recommendService;

  @Autowired
  EntityManager em;

  QRecommend qRecommend = recommend;
  QComment qComment = comment;
  QTrack qTrack = QTrack.track;

  int expirationTimeInWeeks;   // 추천글 유효기간 설정(week)
  LocalDateTime validUntilThisTime; //  추천글 유효기간 기준시간
  int limitSize;

  JPAQueryFactory queryFactory;



  @BeforeEach
  public void setup() {

//    // 스코어가 null 인 추천글만 스코어 갱신
    recommendService.updateScoreIfNull();

    expirationTimeInWeeks = 1;

    validUntilThisTime = LocalDateTime.now().minusWeeks(expirationTimeInWeeks);

    limitSize = 10;

    queryFactory = new JPAQueryFactory(em);


  }

  @Test
  void getRecommendChartByScore() throws Exception {
    List<Tuple> findChart = queryFactory
        .select(
            recommend.recommendNo,
            recommend.recommendTitle,
            recommend.member.memberNo,
            recommend.member.nickName,
            recommend.recommendScore
        )
        .from(recommend)
        .leftJoin(recommend.comments, comment)
        .on(recommend.recommendNo.eq(comment.recommend.recommendNo))
        .where(recommend.createdAt.after(validUntilThisTime))
        .groupBy(recommend.recommendNo)
        .orderBy(recommend.recommendScore.desc(), recommend.recommendViews.desc(),
            recommend.recommendTitle.asc(),
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
      Long recommendScore = Long.valueOf(String.valueOf(tuple.get(recommend.recommendScore)));

      Long recommendViews = Long.valueOf(String.valueOf(tuple.get(recommend.recommendViews)));

      responses.add(
          new RecommendChartResponse(recommendNo, recommendTitle, memberNo, memberNickName,
              recommendScore,
              recommendViews));
    }

    for (Tuple tuple : findChart) {
      System.out.println(tuple);
    }

    Assertions.assertEquals(findChart.size(), 7);

  }


//  @Test
//  void getRecommendChart() throws Exception {
//
//    // 별칭 지정
//    StringPath totalScore = Expressions.stringPath("totalScore");
//
//    /*
//     * 추천글 left join 댓글
//     * 추천글 번호, 추천글 제목, 추천글 작성자 이름, 추천글 총합 점수("score"), 추천글 조회수
//     * score = 추천글 포인트 + 좋아요 갯수 + 댓글 갯수 + 댓글 포인트 총합
//     * 정렬 기준: 1. score 2. 조회수 3. 제목 이름순 4. 추천글 번호순 (오름차순)
//     */
//
////    List<Tuple> findChart = queryFactory
////        .select(
////            qRecommend.recommendTitle,
////            qRecommend.member.nickName,
////            qRecommend.recommendNo)
////        .from(qRecommend)
////        .fetch();
//
//    List<Tuple> findChart = queryFactory
//        .select(
//            qRecommend.recommendNo,
//            qRecommend.recommendTitle,
//            qRecommend.member.nickName,
//            (qRecommend.recommendPoint.coalesce(0L).add(qRecommend.likes.size())
//                .add(qRecommend.comments.size()).add(qComment.commentPoint.coalesce(0L).sum())).as(
//                "totalScore"),
//            qRecommend.recommendViews)
//        .from(qRecommend)
//        .leftJoin(qRecommend.comments, qComment)
//        .on(qRecommend.recommendNo.eq(qComment.recommend.recommendNo))
//        .where(qRecommend.createdAt.after(validUntilThisTime))
//        .groupBy(qRecommend.recommendNo)
//        .orderBy(totalScore.desc(), qRecommend.recommendViews.desc(),
//            qRecommend.recommendTitle.asc(), qRecommend.recommendNo.asc())
//        .limit(limitSize)
//        .fetch();
//
////    List<Tuple> findChart = queryFactory
////        .select(qRecommend.recommendTitle, qRecommend.member.nickName,
////            qRecommend.recommendPoint.coalesce(0L).add(sum(qComment.commentPoint.coalesce(0L)))
////                .add(qRecommend.likes.size().coalesce(0))
////                .as("score"), qRecommend.recommendViews, qRecommend.recommendNo)
////        .distinct()
////        .from(qRecommend, qComment)
////        .where(qRecommend.recommendNo.eq(qComment.recommend.recommendNo))
//////        .where(qRecommend.createdAt.after(threeMonthsAgo))
////        .orderBy(totalScore.desc())
////        .fetch();
//
////    List<Tuple> findChart = queryFactory
////        .select(qRecommend.recommendTitle, qRecommend.recommendPoint.add(sum(qComment.commentPoint)).add(qLikes.count()).as("score"))
////        .from(qRecommend, qComment, qLikes)
////        .where(qRecommend.recommendNo.eq(qComment.recommend.recommendNo), qRecommend.recommendNo.eq(qLikes.recommend.recommendNo))
////        .orderBy(totalScore.desc())
//////        .where(qRecommend.createdAt.after(threeMonthsAgo))
////        .fetch();
//
//    for (Tuple tuple : findChart) {
//      System.out.println(tuple);
//    }
//
//    Assertions.assertEquals(findChart.size(), 7);
//  }
//
////  @Test
////  void getTrackChart() throws Exception {
////    JPAQueryFactory queryFactory = new JPAQueryFactory(em);
////
////    // 별칭 지정
////    StringPath totalScore = Expressions.stringPath("totalScore");
////
////    int expirationTimeInWeeks = 12;
////
////    // 추천글 유효기간
////    LocalDateTime validUntilThisTime = LocalDateTime.now().minusWeeks(expirationTimeInWeeks);
////
////    /*
////     * 트랙 left join 추천글
////     * 음원 번호, 트랙 제목, 아티스트 이름, 대표 추천인, 해당 트랙의 추천글 포인트(score) 총합
////     * score = 추천글 포인트 + 좋아요 갯수 + 댓글 갯수 + 댓글 포인트 총합
////     * 정렬 기준: 1. score 총합 2. 트랙 제목 이름순 3. 아티스트 이름순
////     */
////
////    List<Tuple> findChart = queryFactory
////        .select(
////            recommend.track.trackNo,
////            recommend.track.trackTitle,
////            recommend.track.album.albumNo,
////            recommend.track.album.albumImageUrl,
////            recommend.track.artist.artistNo,
////            recommend.track.artist.artistName,
////            (recommend.recommendPoint
////                .add(comment.commentPoint.coalesce(0L).sum())
////                .add(recommend.comments.size())
////                .add(recommend.likes.size())).as("totalScore")
////        )
////        .from(recommend)
////        .leftJoin(recommend.comments, comment)
////        .on(recommend.recommendNo.eq(comment.recommend.recommendNo))
////        .where(recommend.createdAt.after(validUntilThisTime))
////        .groupBy(recommend.track.trackNo)
////        .orderBy(totalScore.desc(), recommend.track.trackTitle.asc(),
////            recommend.track.artist.artistName.asc())
////        .limit(limitSize)
////        .fetch();
////
////    for (Tuple tuple : findChart) {
////      System.out.println(tuple);
////    }
////
////    Assertions.assertEquals(findChart.size(), 3);
////
////  }

}