package com.content_i_like.service;

import com.content_i_like.domain.entity.Album;
import com.fasterxml.jackson.databind.JsonNode;

public class AlbumFetch implements Fetch<Album> {
    @Override
    public String extractTitle(JsonNode root, int count) {
        return root.at("/tracks/album/" + count + "/name").asText();
    }
}
