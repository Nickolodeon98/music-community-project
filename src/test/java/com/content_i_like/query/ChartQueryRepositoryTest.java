package com.content_i_like.query;

import static com.querydsl.core.group.GroupBy.sum;

import com.content_i_like.domain.entity.Album;
import com.content_i_like.domain.entity.Artist;
import com.content_i_like.domain.entity.Comment;
import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.QComment;
import com.content_i_like.domain.entity.QLikes;
import com.content_i_like.domain.entity.QRecommend;
import com.content_i_like.domain.entity.QTrack;
import com.content_i_like.domain.entity.Recommend;
import com.content_i_like.domain.entity.Track;
import com.content_i_like.fixture.Fixture;
import com.content_i_like.repository.RecommendRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ChartQueryRepositoryTest {

  RecommendRepository recommendRepository;

  @Autowired
  EntityManager em;

  QRecommend qRecommend = QRecommend.recommend;
  QComment qComment = QComment.comment;
  QTrack qTrack = QTrack.track;

  int expirationTimeInWeeks;   // 추천글 유효기간 설정(week)
  LocalDateTime validUntilThisTime; //  추천글 유효기간 기준시간
  int limitSize;

  JPAQueryFactory queryFactory;


  @BeforeEach
  public void setup() {

    expirationTimeInWeeks = 4;

    validUntilThisTime = LocalDateTime.now().minusWeeks(expirationTimeInWeeks);

    limitSize = 10;

    queryFactory = new JPAQueryFactory(em);


  }

  @Test
  void getRecommendChart() throws Exception {

    // 별칭 지정
    StringPath totalScore = Expressions.stringPath("totalScore");

    /*
     * 추천글 left join 댓글
     * 추천글 번호, 추천글 제목, 추천글 작성자 이름, 추천글 총합 점수("score"), 추천글 조회수
     * score = 추천글 포인트 + 좋아요 갯수 + 댓글 갯수 + 댓글 포인트 총합
     * 정렬 기준: 1. score 2. 조회수 3. 제목 이름순 4. 추천글 번호순 (오름차순)
     */

//    List<Tuple> findChart = queryFactory
//        .select(
//            qRecommend.recommendTitle,
//            qRecommend.member.nickName,
//            qRecommend.recommendNo)
//        .from(qRecommend)
//        .fetch();

    List<Tuple> findChart = queryFactory
        .select(
            qRecommend.recommendNo,
            qRecommend.recommendTitle,
            qRecommend.member.nickName,
            (qRecommend.recommendPoint.coalesce(0L).add(qRecommend.likes.size())
                .add(qRecommend.comments.size()).add(qComment.commentPoint.coalesce(0L).sum())).as(
                "totalScore"),
            qRecommend.recommendViews)
        .from(qRecommend)
        .leftJoin(qRecommend.comments, qComment)
        .on(qRecommend.recommendNo.eq(qComment.recommend.recommendNo))
        .where(qRecommend.createdAt.after(validUntilThisTime))
        .groupBy(qRecommend.recommendNo)
        .orderBy(totalScore.desc(), qRecommend.recommendViews.desc(),
            qRecommend.recommendTitle.asc(), qRecommend.recommendNo.asc())
        .limit(limitSize)
        .fetch();

//    List<Tuple> findChart = queryFactory
//        .select(qRecommend.recommendTitle, qRecommend.member.nickName,
//            qRecommend.recommendPoint.coalesce(0L).add(sum(qComment.commentPoint.coalesce(0L)))
//                .add(qRecommend.likes.size().coalesce(0))
//                .as("score"), qRecommend.recommendViews, qRecommend.recommendNo)
//        .distinct()
//        .from(qRecommend, qComment)
//        .where(qRecommend.recommendNo.eq(qComment.recommend.recommendNo))
////        .where(qRecommend.createdAt.after(threeMonthsAgo))
//        .orderBy(totalScore.desc())
//        .fetch();

//    List<Tuple> findChart = queryFactory
//        .select(qRecommend.recommendTitle, qRecommend.recommendPoint.add(sum(qComment.commentPoint)).add(qLikes.count()).as("score"))
//        .from(qRecommend, qComment, qLikes)
//        .where(qRecommend.recommendNo.eq(qComment.recommend.recommendNo), qRecommend.recommendNo.eq(qLikes.recommend.recommendNo))
//        .orderBy(totalScore.desc())
////        .where(qRecommend.createdAt.after(threeMonthsAgo))
//        .fetch();

    for (Tuple tuple : findChart) {
      System.out.println(tuple);
    }

    Assertions.assertEquals(findChart.size(), 7);
  }


  @Test
  void getTrackChart() throws Exception {
    JPAQueryFactory queryFactory = new JPAQueryFactory(em);

    // 별칭 지정
    StringPath totalScore = Expressions.stringPath("totalScore");

    int expirationTimeInWeeks = 4;

    // 추천글 유효기간
    LocalDateTime validUntilThisTime = LocalDateTime.now().minusWeeks(expirationTimeInWeeks);

    /*
     * 트랙 left join 추천글
     * 음원 번호, 트랙 제목, 아티스트 이름, 대표 추천인, 해당 트랙의 추천글 포인트(score) 총합
     * score = 추천글 포인트 + 좋아요 갯수 + 댓글 갯수 + 댓글 포인트 총합
     * 정렬 기준: 1. score 총합 2. 트랙 제목 이름순 3. 트랙 제목 이름순 4. 트랙 번호순 (오름차순)
     */

    /*
    SELECT (
        sum(SELECT (sum(comment.point) + recommendation.point)
            FROM recommendation
            LEFT JOIN comment ON recommendation.recommendation_id = comment.recommendation_id
            GROUP BY recommendation_id WHERE recommendation_id = R.recommendation_id)
        as score_sum) AS track_score
    FROM recommendation R LEFT JOIN track ON recommendation.track_id = track.track_id
    GROUP BY track_id

                (qRecommend.recommendPoint.coalesce(0L).add(qRecommend.likes.size())
                .add(qRecommend.comments.size()).add(qComment.commentPoint.coalesce(0L).sum())).as(
                "totalScore"),
            qRecommend.recommendViews)
    */
    QRecommend subRecommend = new QRecommend("subRecommend");
    QComment subComment = new QComment("subComment");
    QTrack subTrack = new QTrack("subTrack");

    List<Tuple> findChart = queryFactory
        .select(
            subRecommend.track.trackNo,
            subRecommend.track.trackTitle,
            subRecommend.track.album.albumImageUrl,
            subRecommend.track.artist.artistNo,
            subRecommend.track.artist.artistName,
            (subRecommend.recommendPoint.coalesce(0L)
                .add(subComment.commentPoint.coalesce(0L).sum())
                .add(subRecommend.comments.size())
                .add(subRecommend.likes.size())).as("totalScore")
        )
        .from(subRecommend)
        .leftJoin(subRecommend.comments, subComment)
        .groupBy(subRecommend.track.trackNo)
        .orderBy(totalScore.desc())
        .fetch();




//    List<Tuple> findChart = queryFactory
//        .select(
//            qTrack.trackNo,
//            qTrack.trackTitle,
//            qTrack.artist.artistNo,
//            qTrack.artist.artistName,
//            ExpressionUtils.as(
//                JPAExpressions
//                    .select(subRecommend.recommendPoint.coalesce(0L).sum())
//                    .leftJoin(subRecommend.track, subTrack)
//                    .on(subRecommend.track.trackNo.eq(subTrack.trackNo))
//                    .groupBy(subTrack)
//                    .from(subRecommend)
//                ,("totalScore")
//            )
//
////            (JPAExpressions
////                .select((subRecommend.recommendPoint.coalesce(0L).add(subRecommend.likes.size())
////                    .add(subRecommend.comments.size()).add(subComment.commentPoint.coalesce(0L).sum())).as(
////                    "totalScore"))
//
////                .distinct()
////                .from(subRecommend)
////                .leftJoin(subRecommend.comments, subComment)
////                .on(subRecommend.recommendNo.eq(subComment.recommend.recommendNo))
////                .where(subRecommend.createdAt.after(validUntilThisTime))
////                .groupBy(subRecommend.recommendNo)
////            )
//        )
//        .from(qRecommend)
////        .leftJoin(qRecommend.track, qTrack)
////        .on(qRecommend.track.trackNo.eq(qTrack.trackNo))
////        .groupBy(qTrack.trackNo)
//        .limit(limitSize)
//        .fetch();

//    List<Tuple> findChart = queryFactory
//        .select(
//            qRecommend.recommendNo,
//            qRecommend.recommendTitle,
//            qRecommend.member.nickName,
//            (qRecommend.recommendPoint.coalesce(0L).add(qRecommend.likes.size()).add(qRecommend.comments.size()).add(qComment.commentPoint.coalesce(0L).sum())).as("totalScore"),
//            qRecommend.recommendViews)
//        .from(qRecommend)
//        .leftJoin(qRecommend.comments, qComment).on(qRecommend.recommendNo.eq(qComment.recommend.recommendNo))
//        .where(qRecommend.createdAt.after(validUntilThisTime))
//        .groupBy(qRecommend.recommendNo)
//        .orderBy(totalScore.desc(), qRecommend.recommendViews.desc(), qRecommend.recommendTitle.asc(), qRecommend.recommendNo.asc())
//        .limit(limitSize)
//        .fetch();

    for (Tuple tuple : findChart) {
      System.out.println(tuple);
    }

    Assertions.assertEquals(findChart.size(), 7);

  }

}