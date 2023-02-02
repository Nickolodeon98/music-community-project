package com.content_i_like.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemSearch<T> {

  Page<T> searchAll(Pageable pageable);

  Page<T> search(String keyword, Pageable pageable);
}
