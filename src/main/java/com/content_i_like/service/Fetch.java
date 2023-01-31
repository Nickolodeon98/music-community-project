package com.content_i_like.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface Fetch<T> {

  String extractData(JsonNode root, int count);

  List<T> parseIntoEntities(List<String> titles);
}
