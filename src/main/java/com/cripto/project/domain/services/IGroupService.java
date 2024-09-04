package com.cripto.project.domain.services;

import java.util.List;
import java.util.Map;

import com.cripto.project.domain.dtos.consumes.GroupDtoRequest;
import com.cripto.project.domain.dtos.produces.group.GroupDtoResponse;

public interface IGroupService extends ICrudService<GroupDtoResponse, GroupDtoRequest> {

    public Map<String, List<GroupDtoResponse>> getGroupByNameContaining(String groupName);
    
}
