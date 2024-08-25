package com.cripto.project.domain.services;

import java.util.List;
import java.util.Map;

public interface CrudService<T, W> {
    public Map<String, T> register(W w);

    public Map<String, List<T>> getAll();

    public Map<String, T> getById(Long id);

    public Map<String, T> update(Long id, W w);

    public Map<String, String> delete(Long id);
}
