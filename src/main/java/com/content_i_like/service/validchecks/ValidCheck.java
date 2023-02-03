package com.content_i_like.service.validchecks;

public interface ValidCheck<T> {
  T examine(String toCheck);
}
