package com.content_i_like.service;

public interface DBSaveOption<T> {

    T buildEntity(String title);
    void saveNewRow(T entity);
}
