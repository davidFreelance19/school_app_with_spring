package com.cripto.project.domain.dtos.produces.user;

import com.cripto.project.domain.entities.UserEntity;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(name = "UserResponse")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoResponse {

    private Long id;
    private String name;
    private String lastname;
    private String email;
    
    public static UserDtoResponse responseDto(UserEntity entity){
        return new UserDtoResponse(entity.getId(), entity.getName(),entity.getLastname(), entity.getEmail());
    }
}
