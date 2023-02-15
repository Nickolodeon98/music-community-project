package com.content_i_like.service.fetchoptions;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Set;

public interface NewFetch<T> {

  String extractData(JsonNode root, int count);

  Set<T> parseIntoEntitiesAllUnique(Set<String> titles);
}
