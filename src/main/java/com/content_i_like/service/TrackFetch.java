package com.content_i_like.service;

import com.content_i_like.domain.entity.Track;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class TrackFetch implements Fetch<Track> {

  @Override
  public String extractData(JsonNode root, int count) {
    String data = "";
    String albumName = "";
    String albumImageUrl = "";
    try {
      albumName = root.get("tracks").get(count).get("album").get("name").asText();
      albumImageUrl = root.get("tracks").get(count).get("album").get("images").get(1).get("url").asText();
      data = root.get("tracks").get(count).get("name").asText();
    } catch (NullPointerException e) {
      log.warn("JsonNode 가 null 값입니다.");
    }
    return data;
  }

  @Override
  public List<Track> parseIntoEntities(List<String> titles) {
    List<Track> tracks = new ArrayList<>();
    for (String title : titles) {
      Track track = Track.builder()
              .trackTitle(title)
              .build();
      tracks.add(track);
    }
    return tracks;
  }
}