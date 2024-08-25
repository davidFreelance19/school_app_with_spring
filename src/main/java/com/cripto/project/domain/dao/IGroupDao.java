package com.cripto.project.domain.dao;

import java.util.List;

import com.cripto.project.domain.entities.GroupEntity;

public interface IGroupDao extends ICrudDao<GroupEntity>{

    public List<GroupEntity> getGroupByNameContaining(String name);
    
}
