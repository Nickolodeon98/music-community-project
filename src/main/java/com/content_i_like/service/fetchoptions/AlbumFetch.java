package com.content_i_like.service.fetchoptions;

import com.content_i_like.domain.entity.Album;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AlbumFetch implements Fetch<Album> {

  @Override
  public String extractData(JsonNode root, int count) {
    String albumName = "";
    String albumImageUrl = "";
    try {
      albumName = root.get("tracks").get(count).get("album").get("name").asText();

      /* 1번 인덱스에서 가져오는 이유는 1번이 height 300, width 300 이미지의 링크를 담고 있기 때문이다. */
      albumImageUrl = root.get("tracks").get(count).get("album").get("images").get(1).get("url")
          .asText();
    } catch (NullPointerException e) {
      log.warn("JsonNode 가 null 값입니다.");
      return "";
    }

    return String.format("%s\n%s", albumName, albumImageUrl);
  }

  @Override
  public List<Album> parseIntoEntities(List<String> titles) {
    List<Album> albums = new ArrayList<>();
    for (String title : titles) {
      String[] titleAndUrl = title.split("\n");
      Album album = Album.builder()
          .albumTitle(titleAndUrl[0])
          .albumImageUrl(titleAndUrl[1])
          .build();
      albums.add(album);
    }
    return albums;
  }
}
