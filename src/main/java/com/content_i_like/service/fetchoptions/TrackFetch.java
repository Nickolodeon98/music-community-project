package com.content_i_like.service.fetchoptions;

import com.content_i_like.domain.entity.Track;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TrackFetch implements Fetch<Track> {

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
      albumName = root.get("tracks").get(count).get("album").get("name").asText();
      albumImageUrl = root.get("tracks").get(count).get("album").get("images").get(1).get("url")
          .asText();
      albumId = root.get("tracks").get(count).get("album").get("id").asText();
      artistNode = root.get("tracks").get(count).get("artists");

      if (artistNode.isArray()) {
        valuesCount = artistNode.size();
      }

      data = root.get("tracks").get(count).get("name").asText();
      albumReleaseDate = root.get("tracks").get(count).get("album").get("release_date").asText();
      totalTracks = root.get("tracks").get(count).get("album").get("total_tracks").asText();

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

  @Override
  public Set<Track> parseIntoEntitiesAllUnique(Set<String> titles) {
    Set<Track> tracks = new HashSet<>();

    for (String title : titles) {
      String[] trackAndAlbumAndArtist = title.split("\n");

      Track track = Track.builder()
          .trackTitle(trackAndAlbumAndArtist[0])
          .albumSpotifyId(trackAndAlbumAndArtist[1])
          .artistName(trackAndAlbumAndArtist[2])
          .trackReleaseDate(trackAndAlbumAndArtist[3].substring(0, 4))
          .albumTotalTracks(trackAndAlbumAndArtist[4])
          .artistSpotifyId(trackAndAlbumAndArtist[5])
          .build();

      tracks.add(track);
    }
    return tracks;
  }
}
