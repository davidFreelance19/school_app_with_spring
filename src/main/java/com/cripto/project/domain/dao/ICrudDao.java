package com.cripto.project.domain.dao;

import java.util.List;


public interface ICrudDao<T> {
    public T register(T t);

    public List<T> getAll();

    public T getById(Long id);

    public T update(Long id, T t);

    public void delete(Long id);
}
