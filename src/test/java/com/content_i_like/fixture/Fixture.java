package com.content_i_like.fixture;

import com.content_i_like.domain.entity.*;
import com.content_i_like.domain.enums.GenderEnum;
import com.content_i_like.domain.enums.MemberStatusEnum;

public class Fixture {

  public static Member getMemberFixture() {
    return Member.builder()
        .memberNo(1L)
        .email("email@naver.com")
        .profileImgUrl("profile")
        .nickName("nickname")
        .name("name")
        .introduction("self")
        .status(MemberStatusEnum.ADMIN)
        .gender(GenderEnum.MALE)
        .birth(1995)
        .build();
  }

  public static Artist getArtistFixture() {
    return Artist.builder()
        .artistNo(1L)
        .artistName("이름")
        .build();
  }

  public static Album getAlbumFixture(Artist artist) {
    return Album.builder()
        .albumNo(1L)
        .albumImageUrl("이미지")
        .artist(artist)
        .build();
  }

  public static Track getTrackFixture(Album album) {
    return Track.builder()
        .trackNo(1L)
        .trackTitle("음원 제목")
        .album(album)
        .build();
  }

  public static Recommend getRecommendFixture(Member member, Track track) {
    return Recommend.builder()
        .recommendNo(1L)
        .recommendTitle("제목")
        .recommendContent("내용")
        .recommendImageUrl("이미지")
        .recommendYoutubeUrl("유튜브")
        .recommendPoint(1000L)
        .recommendViews(100L)
        .member(member)
        .track(track)
        .build();
  }

  public static Comment getCommentFixture(Member member, Recommend recommend) {
    return Comment.builder()
        .commentContent("댓글")
        .commentPoint(100L)
        .member(member)
        .recommend(recommend)
        .build();
  }

}
