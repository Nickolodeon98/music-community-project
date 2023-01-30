package com.content_i_like.service;

import java.util.List;

public interface DBSaveOption<T> {

    T buildEntity(Object title);
    T saveNewRow(T entity);

    List<T> fetchEverything();
}
