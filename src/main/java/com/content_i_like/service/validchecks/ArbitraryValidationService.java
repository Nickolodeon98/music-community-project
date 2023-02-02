package com.content_i_like.service.validchecks;

import com.content_i_like.service.validchecks.ValidCheck;

public class ArbitraryValidationService {
  public static <T> T validate(String toCheck, ValidCheck<T> validCheck) {
    return validCheck.examine(toCheck);
  }
}
