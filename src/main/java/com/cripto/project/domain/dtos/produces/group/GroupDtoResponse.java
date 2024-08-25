package com.cripto.project.domain.dtos.produces.group;

import org.modelmapper.ModelMapper;

import com.cripto.project.domain.entities.GroupEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(name = "GroupResponse")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GroupDtoResponse {
    private Long id;
    private String name;

    public static final GroupDtoResponse responseDto(GroupEntity entity) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(entity, GroupDtoResponse.class);
    }
}
