package com.cripto.project.presentation.controllers.admin;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.cripto.project.domain.services.IGroupService;
import com.cripto.project.presentation.exceptions.GlobalErrorsMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.NoResultException;

import com.cripto.project.domain.dtos.consumes.GroupDtoRequest;
import com.cripto.project.domain.dtos.produces.group.GroupDtoResponse;
import static com.cripto.project.utils.CustomResultMatcher.containsError;

@WebMvcTest(controllers = GroupsController.class)
class GroupControllerTest {

    public static final String INVALID_GROUP_ID = "The param 'groupId' should be a positive number";
    public static final String GROUP_NOT_FOUND = "Group not found";
    public static final String GROUP_ALREDY_EXIST = "Group already exists";
    public static final String GROUP = "group";

    private final String route = "/admin/groups";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IGroupService groupService;


    @Test
    void test_register_group_failed_validation_should_return_bad_request() throws Exception {
        GroupDtoRequest request = new GroupDtoRequest();
        request.setName("TEST"); // Not pattern matching
        
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(route + "/register-group")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson)) 
                .andExpect(status().isBadRequest());
    }

    @Test
    void test_register_group_already_exists_should_return_conflict() throws Exception {
        GroupDtoRequest request = new GroupDtoRequest();
        request.setName("1AV1");

        when(groupService.register(request)).thenThrow(new DataIntegrityViolationException(GROUP_ALREDY_EXIST));

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(route + "/register-group")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson)) 
                .andExpect(status().isConflict())
                .andExpect(containsError(GROUP_ALREDY_EXIST));
    }

    @Test
    void test_register_group_success() throws Exception {
        GroupDtoRequest request = new GroupDtoRequest();
        request.setName("9AV1");
        GroupDtoResponse groupResponse = new GroupDtoResponse(1L, request.getName());
        when(groupService.register(request)).thenReturn(Map.of(GROUP, groupResponse));

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(route + "/register-group")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson)) 
                .andExpect(status().isCreated());
    }

    @Test
    void test_get_group_by_id_successfully() throws Exception {
        GroupDtoResponse groupResponse = new GroupDtoResponse(1L, "1AV1");
        Map<String, GroupDtoResponse> mockResponse = Map.of(GROUP, groupResponse);
        when(groupService.getById(1L)).thenReturn(mockResponse);

        mockMvc.perform(get(route + "/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void test_not_found_group_by_id() throws Exception {
        when(groupService.getById(300L)).thenThrow(new NoResultException(GROUP_NOT_FOUND));

        mockMvc.perform(get(route + "/300")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(containsError(GROUP_NOT_FOUND));
    }

    @Test
    void test_not_positive_group_id_should_return_bad_request() throws Exception {
        mockMvc.perform(get(route + "/0")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(containsError(INVALID_GROUP_ID));
    }

    @Test
    void test_invalid_group_id_should_return_bad_request() throws Exception {
        mockMvc.perform(get(route + "/abc")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(containsError(INVALID_GROUP_ID));
    }

    @Test
    void test_get_all_groups() throws Exception {
        mockMvc.perform(get(route)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void test_get_groups_by_containing_name_success() throws Exception {
        String containingName = "TEST";
        Map<String, List<GroupDtoResponse>> response = Map.of("groups", new ArrayList<>());
        when(groupService.getGroupByNameContaining(containingName)).thenReturn(response);

        mockMvc.perform(get(route + "/get-group?name=" + containingName)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void test_get_groups_by_containing_name_failed() throws Exception {
        mockMvc.perform(get(route + "/get-group")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(containsError("The query parameter name is required"));
    }

    @Test
    void test_update_group_failed_validation_should_return_bad_request() throws Exception {
        Long id = 300L;
        GroupDtoRequest request = new GroupDtoRequest();
        request.setName("9AV3");
        when(groupService.update(id, request)).thenThrow(new NoResultException(GROUP_NOT_FOUND));

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);
        mockMvc.perform(put(route + "/update-group/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test_update_group_not_found_should_return_not_found_exception() throws Exception {
        Long id = 300L;
        GroupDtoRequest request = new GroupDtoRequest();
        request.setName("2JV2");
        when(groupService.update(id, request)).thenThrow(new NoResultException(GROUP_NOT_FOUND));

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);
        mockMvc.perform(put(route + "/update-group/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(containsError(GROUP_NOT_FOUND));
    }

    @Test
    void test_update_group_with_name_already_exists() throws Exception {
        Long id = 1L;
        GroupDtoRequest request = new GroupDtoRequest();
        request.setName("1AV1");
        when(groupService.update(id, request)).thenThrow(new DataIntegrityViolationException(GROUP_ALREDY_EXIST));

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(put(route + "/update-group/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson))
                .andExpect(status().isConflict())
                .andExpect(containsError(GROUP_ALREDY_EXIST));
    }

    @Test
    void test_update_group_success() throws Exception {
        Long id = 1L;
        GroupDtoRequest request = new GroupDtoRequest();
        request.setName("7AV12");
        GroupDtoResponse groupResponse = new GroupDtoResponse();
        groupResponse.setName("7AV12");
        when(groupService.update(id, request)).thenReturn(Map.of(GROUP, groupResponse));

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(put(route + "/update-group/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void test_delete_group_should_return_not_found() throws Exception {
        Long id = 300L;
        when(groupService.delete(id)).thenThrow(new NoResultException(GROUP_NOT_FOUND));

        mockMvc.perform(delete(route + "/delete-group/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(containsError(GROUP_NOT_FOUND));
    }

    @Test
    void test_delete_group_invalid_id_should_return_bad_request() throws Exception {
        String id = "abc";

        mockMvc.perform(delete(route + "/delete-group/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(containsError(INVALID_GROUP_ID));
    }

    @Test
    void test_delete_group_not_positive_id_should_return_bad_request() throws Exception {
        String id = "0";

        mockMvc.perform(delete(route + "/delete-group/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(containsError(INVALID_GROUP_ID));
    }

    @Test
    void test_delete_group_with_dependecies_should_return_conflict() throws Exception {
        Long id = 1L;
        when(groupService.delete(id))
                .thenThrow(new DataIntegrityViolationException(GlobalErrorsMessage.DATA_INTEGRITY_VIOLATION));

        mockMvc.perform(delete(route + "/delete-group/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isConflict())
                .andExpect(containsError(GlobalErrorsMessage.DATA_INTEGRITY_VIOLATION));
    }

    @Test
    void test_delete_group_success() throws Exception {
        Long id = 1L;
        String message = "Group deleted successfully";
        when(groupService.delete(id)).thenReturn(Map.of("message", message));

        mockMvc.perform(delete(route + "/delete-group/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }
}
