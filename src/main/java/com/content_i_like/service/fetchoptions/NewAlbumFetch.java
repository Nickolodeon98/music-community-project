package com.content_i_like.service.fetchoptions;

import com.content_i_like.domain.entity.Album;
import com.content_i_like.domain.entity.NewAlbum;
import com.content_i_like.domain.entity.NewArtist;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NewAlbumFetch implements NewFetch<NewAlbum> {

  @Override
  public String extractData(JsonNode root, int count) {
    StringBuilder artistNames = new StringBuilder();
    StringBuilder artistIds = new StringBuilder();

    String albumName = "";
    String albumImageUrl = "";
    String artistName = "";
    String albumId = "";
    String albumReleaseDate = "";
    String totalTracks = "";
    String artistSpotifyId = "";

    JsonNode artistNode = null;

    int valuesCount = 0;

    try {
      albumName = root.get("tracks").get(count).get("album").get("name").asText();

      /* 1번 인덱스에서 가져오는 이유는 1번이 height 300, width 300 이미지의 링크를 담고 있기 때문이다. */
      albumImageUrl = root.get("tracks").get(count).get("album").get("images").get(1).get("url")
          .asText();

      albumId = root.get("tracks").get(count).get("album").get("id").asText();
      albumReleaseDate = root.get("tracks").get(count).get("album").get("release_date").asText();
      totalTracks = root.get("tracks").get(count).get("album").get("total_tracks").asText();


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

    if (albumName.equals("") || albumImageUrl.equals("") || artistNames.isEmpty() || albumId.equals("")
    || albumReleaseDate.equals("") || totalTracks.equals(""))
      return "";

    return String.format("%s\n%s\n%s\n%s\n%s\n%s\n%s",
        albumName, albumImageUrl, artistNames, albumId, albumReleaseDate, totalTracks, artistIds);
  }

  @Override
  public Set<NewAlbum> parseIntoEntitiesAllUnique(Set<String> titles) {
    Set<NewAlbum> albums = new HashSet<>();

    for (String title : titles) {
      String[] titleAndUrlAndArtistName = title.split("\n");

      NewAlbum album = NewAlbum.builder()
          .albumTitle(titleAndUrlAndArtistName[0])
          .albumImageUrl(titleAndUrlAndArtistName[1])
          .artistName(titleAndUrlAndArtistName[2])
          .albumSpotifyId(titleAndUrlAndArtistName[3])
          .releaseDate(titleAndUrlAndArtistName[4])
          .totalTracks(titleAndUrlAndArtistName[5])
          .artistSpotifyId(titleAndUrlAndArtistName[6])
          .build();

      albums.add(album);
    }
    return albums;
  }
}
