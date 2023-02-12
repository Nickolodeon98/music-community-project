package com.content_i_like.domain.enums;

public enum GenderEnum {
  MALE("남"), FEMALE("여"), UNKNOWN("X");

  private final String description;

  GenderEnum(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
