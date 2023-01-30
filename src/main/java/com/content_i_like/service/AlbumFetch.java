package com.content_i_like.service;

import com.content_i_like.domain.entity.Album;
import com.content_i_like.domain.entity.Song;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AlbumFetch implements Fetch<Album> {

  @Override
  public String extractTitle(JsonNode root, int count) {
//    String albumName = root.at("/tracks/" + count + "/album/name").asText();
    return root.get("tracks").get(count).get("album").get("name").asText();
  }

  @Override
  public List<Album> parseIntoEntities(List<String> titles) {
    List<Album> albums = new ArrayList<>();
    for (String title : titles) {
      Album album = Album.builder()
              .albumTitle(title)
              .build();
      albums.add(album);
    }
    return albums;
  }
}
