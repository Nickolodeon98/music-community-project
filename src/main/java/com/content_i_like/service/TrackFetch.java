package com.content_i_like.service;

import com.content_i_like.domain.entity.Track;
import com.fasterxml.jackson.databind.JsonNode;

public class TrackFetch implements Fetch<Track> {

  @Override
  public String extractTitle(JsonNode root, int count) {
    return root.at("/tracks/" + count + "/name").asText();
  }
}
