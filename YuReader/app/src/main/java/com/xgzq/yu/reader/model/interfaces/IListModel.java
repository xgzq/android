package com.xgzq.yu.reader.model.interfaces;

import java.util.List;

public interface IListModel<T> extends IBaseModel {

    List<T> getList();

    T get(int position);
}
