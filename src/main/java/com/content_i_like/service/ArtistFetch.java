package com.content_i_like.service;

import com.content_i_like.domain.entity.Artist;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ArtistFetch implements Fetch<Artist> {

  @Override
  public String extractTitle(JsonNode root, int count) {
    String artistName = root.get("tracks").get(count).get("artists").get(0).get("name").asText();
    log.info("artistName:{}", artistName);
    return artistName;
  }
}
