package com.cripto.project.domain.services;

import java.util.List;
import java.util.Map;

public interface ICrudService<T, W> {
    default Map<String, T> register(W w) {
        return Map.of();
    }

    default Map<String, List<T>> getAll(){
        return Map.of();
    }

    public Map<String, T> getById(Long id);

    public Map<String, T> update(Long id, W w);

    public Map<String, String> delete(Long id);
}
