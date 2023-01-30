package com.content_i_like.service;

import com.content_i_like.domain.entity.Album;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlbumFetch implements Fetch<Album> {

  @Override
  public String extractTitle(JsonNode root, int count) {
//    String albumName = root.at("/tracks/" + count + "/album/name").asText();
    String albumName = root.get("tracks").get(count).get("album").get("name").asText();

    return albumName;
  }
}
