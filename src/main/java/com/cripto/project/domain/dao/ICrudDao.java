package com.cripto.project.domain.dao;

import java.util.List;


public interface ICrudDao<T> {
    public T register(T t);

    default List<T> getAll(){
        return List.of();
    }

    public T getById(Long id);

    public T update(Long id, T t);

    public void delete(Long id);
}
