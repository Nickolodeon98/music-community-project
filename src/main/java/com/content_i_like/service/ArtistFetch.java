package com.content_i_like.service;

import com.content_i_like.domain.entity.Artist;
import com.content_i_like.domain.entity.Song;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ArtistFetch implements Fetch<Artist> {

  @Override
  public String extractTitle(JsonNode root, int count) {
    StringBuilder artistNames = new StringBuilder();
    String artistName = "";

    JsonNode artistNode = root.get("tracks").get(count).get("artists");

    int valuesCount = 0;
    if (artistNode.isArray())
      for (JsonNode element : artistNode) {
        valuesCount++;
      }

    for (int i = 0; i < valuesCount; i++) {
      artistName = artistNode.get(i).get("name").asText();
      artistNames.append(artistName);
      if (i != valuesCount-1) artistNames.append(", ");
    }

    return artistNames.toString();
  }

  @Override
  public List<Artist> parseIntoEntities(List<String> titles) {
    List<Artist> artists = new ArrayList<>();
    for (String title : titles) {
      Artist artist = Artist.builder()
              .artistName(title)
              .build();
      artists.add(artist);
    }
    return artists;
  }
}
