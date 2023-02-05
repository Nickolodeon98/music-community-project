package com.content_i_like.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheService {

  /* It checks whether the method has already been executed for the given argument or not */


  private final ConcurrentMapCacheManager cacheManager;
  @CachePut(value = "keywords", cacheManager = "cacheManager")
  public String cacheTest() {
    return "hello";
  }

  public String getCacheManager() {
    return cacheManager.getCache("keywords").get("hello", String.class);
  }
}
