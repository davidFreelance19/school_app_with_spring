package com.cripto.project.datasource.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.cripto.project.domain.dao.IGroupDao;
import com.cripto.project.domain.dtos.consumes.GroupDtoRequest;
import com.cripto.project.domain.dtos.produces.group.GroupDtoResponse;
import com.cripto.project.domain.dtos.produces.group.GroupWithCoursesDtoResponse;
import com.cripto.project.domain.entities.GroupEntity;
import com.cripto.project.domain.services.IGroupService;
import com.cripto.project.presentation.exceptions.GlobalErrorsMessage;

import jakarta.persistence.NoResultException;

@Service
public class GroupServiceImpl implements IGroupService {

    private static final String GROUPS = "groups";
    private static final String GROUP = "group";

    private final IGroupDao groupDao;

    GroupServiceImpl(IGroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Override
    public final Map<String, GroupDtoResponse> register(GroupDtoRequest dto) {
        try {
            ModelMapper modelMapper = new ModelMapper();
            GroupEntity entity = modelMapper.map(dto, GroupEntity.class);

            GroupDtoResponse response = GroupDtoResponse.responseDto(this.groupDao.register(entity));
            return Map.of(GROUP, response);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Group already exists");
        } 
    }

    @Override
    public final Map<String, List<GroupDtoResponse>> getAll() {
        List<GroupDtoResponse> groups = this.groupDao.getAll().stream()
        .map(GroupDtoResponse::responseDto).collect(Collectors.toList());

        return Map.of(GROUPS, groups);
    }

    @Override
    public final Map<String, List<GroupDtoResponse>> getGroupByNameContaining(String name) {
        List<GroupDtoResponse> groups =  this.groupDao.getGroupByNameContaining(name).stream()
        .map(GroupDtoResponse::responseDto).collect(Collectors.toList());
        
        return Map.of(GROUPS, groups);
    }

    @Override
    public final Map<String, GroupDtoResponse> getById(Long groupId) {
        try {
            GroupDtoResponse response = GroupWithCoursesDtoResponse.response(this.groupDao.getById(groupId));
            return Map.of(GROUP, response);
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        }
    }

    @Override
    public final Map<String, GroupDtoResponse> update(Long groupId, GroupDtoRequest dto) {
        try {
            ModelMapper modelMapper = new ModelMapper();
            GroupEntity entity = modelMapper.map(dto, GroupEntity.class);

            GroupDtoResponse response = GroupDtoResponse.responseDto(this.groupDao.update(groupId, entity));
            return Map.of(GROUP, response);
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Group already exists");
        } 
    }

    @Override
    public final Map<String, String> delete(Long groupId) {
        try {
            this.groupDao.delete(groupId);
            
            return Map.of("message", "Group deleted successfully");
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        }catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(GlobalErrorsMessage.DATA_INTEGRITY_VIOLATION);
        }
    }

}
