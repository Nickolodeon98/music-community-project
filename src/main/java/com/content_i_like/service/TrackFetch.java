package com.content_i_like.service;

import com.content_i_like.domain.entity.Song;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class TrackFetch implements Fetch<Song> {

  @Override
  public String extractTitle(JsonNode root, int count) {
    return root.get("tracks").get(count).get("name").asText();
  }

  @Override
  public List<Song> parseIntoEntities(List<String> titles) {
    List<Song> songs = new ArrayList<>();
    for (String title : titles) {
      Song song = Song.builder()
              .songTitle(title)
              .build();
      songs.add(song);
    }
    return songs;
  }
}
