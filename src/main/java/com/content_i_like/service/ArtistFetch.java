package com.content_i_like.service;

import com.content_i_like.domain.entity.Artist;
import com.fasterxml.jackson.databind.JsonNode;

public class ArtistFetch implements Fetch<Artist> {
    @Override
    public String extractTitle(JsonNode root, int count) {
        return root.at("/tracks/artists/" + count + "/name").asText();
    }
}
