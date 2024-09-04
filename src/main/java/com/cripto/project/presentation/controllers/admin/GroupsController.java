package com.cripto.project.presentation.controllers.admin;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cripto.project.domain.dtos.consumes.GroupDtoRequest;
import com.cripto.project.domain.dtos.produces.group.GroupDtoResponse;
import com.cripto.project.domain.services.IGroupService;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@Tag(name = "Admin-Groups")
@RestController
@RequestMapping("/admin/groups")
public class GroupsController {

    private final IGroupService groupService;

    GroupsController(IGroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping(value = "/register-group", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<Map<String, GroupDtoResponse>> registerGroup(
        @RequestBody @Valid GroupDtoRequest groupDto
    ) {
        return new ResponseEntity<>(this.groupService.register(groupDto), HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity< Map<String, List<GroupDtoResponse>> > getGroups() {
        return new ResponseEntity<>(this.groupService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/get-group", produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<Map<String, List<GroupDtoResponse>>> getGroupByName(
        @RequestParam String name
    ) {
        return new ResponseEntity<>(this.groupService.getGroupByNameContaining(name), HttpStatus.OK);
    }

    @GetMapping(value = "/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<Map<String, GroupDtoResponse>> getGroupById(
        @PathVariable @Positive(message = "{Invalid.id.group}") Long groupId
    ) {
        return new ResponseEntity<>(this.groupService.getById(groupId), HttpStatus.OK);
    }

    @PutMapping(value = "/update-group/{groupId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<Map<String, GroupDtoResponse>> updateGroup(
        @PathVariable @Positive(message = "{Invalid.id.group}") Long groupId,
        @RequestBody @Valid GroupDtoRequest groupDto
    ) {
        return new ResponseEntity<>(this.groupService.update(groupId, groupDto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete-group/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<Map<String, String>> deleteGroup(
        @PathVariable @Positive(message = "{Invalid.id.group}") Long groupId
    ) {
        return new ResponseEntity<>(this.groupService.delete(groupId), HttpStatus.OK);
    }
   
}
