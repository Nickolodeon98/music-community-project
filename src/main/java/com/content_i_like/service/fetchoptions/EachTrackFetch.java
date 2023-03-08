package com.content_i_like.service.fetchoptions;

import com.content_i_like.domain.entity.Track;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EachTrackFetch extends TrackFetch {

  @Override
  public String extractData(JsonNode root, int count) {
    StringBuilder artistNames = new StringBuilder();
    StringBuilder artistIds = new StringBuilder();

    String data = "";
    String albumName = "";
    String albumImageUrl = "";
    String albumId = "";
    String artistName = "";
    String albumReleaseDate = "";
    String totalTracks = "";
    String artistSpotifyId = "";

    JsonNode artistNode = null;

    int valuesCount = 0;

    try {
      albumName = root.get("tracks").get("items").get(count).get("album").get("name").asText();
      log.info("albumName:{}", albumName);
      albumImageUrl = root.get("tracks").get("items").get(count).get("album").get("images").get(1).get("url")
          .asText();
      log.info("albumImageUrl:{}", albumImageUrl);
      albumId = root.get("tracks").get("items").get(count).get("album").get("id").asText();
      log.info("albumId:{}", albumId);
      artistNode = root.get("tracks").get("items").get(count).get("artists");
      log.info("artistNode:{}", artistNode);

      if (artistNode.isArray()) {
        valuesCount = artistNode.size();
      }

      data = root.get("tracks").get("items").get(count).get("name").asText();
      log.info("data:{}", data);
      albumReleaseDate = root.get("tracks").get("items").get(count).get("album").get("release_date").asText();
      log.info("albumReleaseDate:{}", albumReleaseDate);
      totalTracks = root.get("tracks").get("items").get(count).get("album").get("total_tracks").asText();
      log.info("totalTracks:{}", totalTracks);

    } catch (NullPointerException e) {
      log.warn("JsonNode 가 null 값입니다.");
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

    if (data.equals("") || albumId.equals("") || artistNames.isEmpty())
      return "";

    return String.format("%s\n%s\n%s\n%s\n%s\n%s",
        data, albumId, artistNames, albumReleaseDate, totalTracks, artistIds);
  }
}
