package com.content_i_like.service;

import com.content_i_like.domain.entity.Song;
import com.fasterxml.jackson.databind.JsonNode;

public class TrackFetch implements Fetch<Song> {

  @Override
  public String extractTitle(JsonNode root, int count) {
    return root.at("/tracks/" + count + "/name").asText();
  }
}
