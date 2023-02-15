package com.content_i_like.service.fetchoptions;

import com.content_i_like.domain.entity.Artist;
import com.content_i_like.domain.entity.NewArtist;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NewArtistFetch implements NewFetch<NewArtist> {

  @Override
  public String extractData(JsonNode root, int count) {
    StringBuilder artistNames = new StringBuilder();
    StringBuilder artistIds = new StringBuilder();
    String artistName = "";
    JsonNode artistNode = null;
    String albumName = "";
    String albumImageUrl = "";
    String artistSpotifyId = "";

    int valuesCount = 0;
    try {
      albumName = root.get("tracks").get(count).get("album").get("name").asText();
      albumImageUrl = root.get("tracks").get(count).get("album").get("images").get(1).get("url")
          .asText();

      artistNode = root.get("tracks").get(count).get("artists");
      if (artistNode.isArray()) {
        valuesCount = artistNode.size();
      }
    } catch (NullPointerException e) {
      log.warn("JsonNode 가 null 값입니다.");
      return "";
    }

    for (int i = 0; i < valuesCount; i++) {
      artistName = artistNode.get(i).get("name").asText();
      artistSpotifyId = artistNode.get(i).get("id").asText();
      artistNames.append(artistName);
      artistIds.append(artistSpotifyId);
      if (i != valuesCount - 1) {
        artistNames.append(", ");
        artistIds.append(", ");
      }
    }

    return String.format("%s\n%s", artistNames, artistIds);
  }

  @Override
  public Set<NewArtist> parseIntoEntitiesAllUnique(Set<String> titles) {
    Set<NewArtist> artists = new HashSet<>();

    for (String title : titles) {
      String[] artistNameAndId = title.split("\n");


      NewArtist artist = NewArtist.builder()
          .artistName(artistNameAndId[0])
          .artistSpotifyId(artistNameAndId[1])
          .build();
      artists.add(artist);
    }

    return artists;
  }
}
