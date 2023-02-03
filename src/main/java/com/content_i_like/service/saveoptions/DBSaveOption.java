package com.content_i_like.service.saveoptions;

import java.util.List;

public interface DBSaveOption<T> {

  T saveNewRow(T entity);

  List<T> saveNewRows(List<T> entities);

  List<T> fetchEverything();
}
