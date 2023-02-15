package com.content_i_like.repository;

import com.content_i_like.domain.entity.Member;
import com.content_i_like.domain.entity.Recommend;
import com.content_i_like.domain.entity.Track;
import com.content_i_like.repository.custom.CustomRecommendRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecommendRepository extends JpaRepository<Recommend, Long>,
    CustomRecommendRepository {

  @Override
  @Modifying
  <S extends Recommend> S save(S entity);

  @Modifying(clearAutomatically = true)
  @Query("update Recommend r set r.recommendTitle = :title, r.recommendContent = :content, r.recommendImageUrl = :image, r.recommendYoutubeUrl = :youtube  ,r.lastModifiedAt = current_timestamp where r.recommendNo = :recommendNo ")
  void update(@Param("title") String recommendTitle, @Param("content") String recommendContent,
      @Param("image") String recommendImageUrl, @Param("youtube") String recommendYoutubeUrl,
      @Param("recommendNo") Long recommendNo);

  Page<Recommend> findAllByMember(Pageable pageable, Member member);

  Long countByMember(Member member);

  Optional<Page<Recommend>> findByRecommendTitleContaining(String keyword, Pageable pageable);

  Optional<Page<Recommend>> findAllByMemberNickNameContaining(String keyword, Pageable pageable);

  //@Query("SELECT r, COUNT(l) FROM Recommend r LEFT JOIN r.likes l GROUP BY r ORDER BY COUNT(l) DESC")
  @Query(value =
      "SELECT rec.recommendNo, rec.recommendTitle, rec.recommendImageUrl, mem.nickName as memberNickname, "
          + "tr.trackTitle, al.albumImageUrl, ar.artistName, rec.recommendContent, "
          + "COUNT(l.likesNo) as countLikes, "
          + "COALESCE((SELECT SUM(c.commentPoint) + rec.recommendPoint "
          + "FROM Comment c "
          + "WHERE c.recommend = rec), rec.recommendPoint) as accumulatedPoints "
          + "FROM Recommend rec "
          + "LEFT JOIN Member mem ON rec.member = mem "
          + "LEFT JOIN Track tr ON rec.track = tr "
          + "LEFT JOIN Album al ON tr.album = al "
          + "LEFT JOIN Artist ar ON al.artist = ar "
          + "LEFT JOIN Likes l ON rec.recommendNo = l.recommend.recommendNo "
          + "GROUP BY rec.recommendNo "
          + "ORDER BY countLikes DESC",
      countQuery = "SELECT COUNT(rec) "
          + "FROM Recommend rec "
          + "LEFT JOIN Member mem ON rec.member = mem "
          + "LEFT JOIN Track tr ON rec.track = tr "
          + "LEFT JOIN Album al ON tr.album = al "
          + "LEFT JOIN Artist ar ON al.artist = ar "
          + "LEFT JOIN Likes l ON rec.recommendNo = l.recommend.recommendNo")
  Page<Object[]> findAllWithLikeCount(Pageable pageable);

  @Query(value =
      "SELECT rec.recommendNo, rec.recommendTitle, rec.recommendImageUrl, mem.nickName as memberNickname, "
          + "tr.trackTitle, al.albumImageUrl, ar.artistName, rec.recommendContent, "
          + "COUNT(l.likesNo) as countLikes, "
          + "COALESCE((SELECT SUM(c.commentPoint) + rec.recommendPoint FROM Comment c WHERE c.recommend = rec), rec.recommendPoint) as accumulatedPoints "
          + "FROM Recommend rec "
          + "LEFT JOIN Member mem ON rec.member = mem "
          + "LEFT JOIN Track tr ON rec.track = tr "
          + "LEFT JOIN Album al ON tr.album = al "
          + "LEFT JOIN Artist ar ON al.artist = ar "
          + "LEFT JOIN Likes l ON rec.recommendNo = l.recommend.recommendNo "
          + "GROUP BY rec.recommendNo, rec.recommendPoint "
          + "ORDER BY accumulatedPoints DESC",
      countQuery = "SELECT COUNT(rec) "
          + "FROM Recommend rec "
          + "LEFT JOIN Member mem ON rec.member = mem "
          + "LEFT JOIN Track tr ON rec.track = tr "
          + "LEFT JOIN Album al ON tr.album = al "
          + "LEFT JOIN Artist ar ON al.artist = ar "
          + "LEFT JOIN Likes l ON rec.recommendNo = l.recommend.recommendNo")
  Page<Object[]> findAllWithAccumulatedPoints(Pageable pageable);

  Optional<List<Recommend>> findAllByTrack(Track track);
}
