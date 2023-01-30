package com.content_i_like.service;

public interface DBSaveOption<T> {

    T buildEntity(String title);
    T saveNewRow(T entity);
}
