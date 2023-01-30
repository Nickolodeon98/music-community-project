package com.content_i_like.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface Fetch<T> {

    String extractTitle(JsonNode root, int count);

}
