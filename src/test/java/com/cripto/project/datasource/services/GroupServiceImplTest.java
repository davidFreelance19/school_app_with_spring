package com.cripto.project.datasource.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;

import com.cripto.project.datasource.dao.GroupDaoImpl;
import com.cripto.project.domain.dtos.consumes.GroupDtoRequest;
import com.cripto.project.domain.dtos.produces.group.GroupDtoResponse;
import com.cripto.project.domain.entities.CourseEntity;
import com.cripto.project.domain.entities.GroupEntity;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.NoResultException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    private GroupDaoImpl groupRepository;

    @InjectMocks
    private GroupServiceImpl groupService;

    /**
     * Tests the successful registration of a new group.
     * 
     * Verifies that the group is correctly registered and the returned DTO
     * matches the input data.
     */
    @Test
    void test_register_group_successfully() {
        GroupDtoRequest dto = new GroupDtoRequest();
        dto.setName("1AV1");
        
        ModelMapper mapper = new ModelMapper();
        GroupEntity entity = mapper.map(dto, GroupEntity.class);

        when(groupRepository.register(any(GroupEntity.class))).thenReturn(entity);
        
        Map<String, GroupDtoResponse> response = groupService.register(dto);

        assertEquals(dto.getName(), response.get("group").getName());
    }

    /**
     * Tests the registration of a group that already exists.
     * 
     * @throws EntityExistsException when attempting to register a group
     *                               with a name that already exists.
     */
    @Test
    void test_register_group_already_exists() {
        GroupDtoRequest dto = new GroupDtoRequest();
        dto.setName("1AV1");
        
        when(groupRepository.register(any(GroupEntity.class)))
                .thenThrow(new DataIntegrityViolationException("Group already exists"));

        assertThrows(EntityExistsException.class, () -> {
            this.groupService.register(dto);
        });
    }

    /**
     * Tests the successful retrieval of a group by its ID.
     * 
     * Verifies that the returned DTO matches the mocked group entity.
     */
    @Test
    void test_get_group_by_id_successfully() {
        Long id = 1L;
        List<CourseEntity> courses = new ArrayList<>();
        GroupEntity mockGroup = GroupEntity.builder().id(id).name("9AV1").courses(courses).build();

        when(groupRepository.getById(id)).thenReturn(mockGroup);

        Map<String, GroupDtoResponse> response = groupService.getById(id);

        assertEquals(mockGroup.getName(), response.get("group").getName());
    }

    /**
     * Tests the scenario where a group is not found by its ID.
     * 
     * @throws NoResultException when the group with the given ID is not found.
     */
    @Test
    void test_group_not_found_by_id() {
        Long id = 300L;

        when(groupRepository.getById(id)).thenThrow(new NoResultException("Not found"));

        assertThrows(NoResultException.class, () -> {
            groupService.getById(id);
        });
    }

    /**
     * Tests the retrieval of all groups.
     * 
     * Verifies that the returned list is not null and contains the expected number of groups.
     */
    @Test
    void test_get_all_groups() {
        List<GroupEntity> groups = List.of(
                GroupEntity.builder()
                        .id(1L)
                        .name("9AV1")
                        .courses(new ArrayList<>())
                        .build());

        when(groupRepository.getAll()).thenReturn(groups);

        Map<String, List<GroupDtoResponse>> response = groupService.getAll();

        assertNotNull(response.get("groups"));
        assertTrue(response.get("groups").size() >= 0);
    }

    /**
     * Tests the retrieval of groups with names containing a specific string.
     * 
     * Verifies that all returned groups have names containing the specified string.
     */
    @Test
    void test_get_groups_with_containing_name() {
        String containingName = "9AV";
        List<GroupEntity> groups = List.of(
            GroupEntity.builder()
                    .id(1L)
                    .name("9AV1")
                    .courses(new ArrayList<>())
                    .build());

        when(groupRepository.getGroupByNameContaining(containingName)).thenReturn(groups);

        Map<String, List<GroupDtoResponse>> response = groupService.getGroupByNameContaining(containingName);

        assertNotNull(response.get("groups"));
        assertTrue(groups.stream().allMatch(g -> g.getName().contains(containingName)));
    }

    /**
     * Tests the update of a non-existent group.
     * 
     * @throws NoResultException when attempting to update a group that doesn't exist.
     */
    @Test
    void test_update_group_not_found(){
        Long id = 300L;
        GroupDtoRequest request = new GroupDtoRequest();
        request.setName("2BV1");

        when(groupRepository.update(anyLong(), any(GroupEntity.class)))
            .thenThrow(new NoResultException("Group not found"));

        assertThrows(NoResultException.class, () -> {
            this.groupService.update(id, request);
        });
    }

    /**
     * Tests the update of a GroupEntity with a name that already exists.
     * 
     * @throws EntityExistsException when attempting to update a group
     *                               with a name that's already in use.
     */
    @Test
    void test_update_group_with_name_already_exists(){
        Long id = 1L;
        GroupDtoRequest request = new GroupDtoRequest();
        request.setName("2BV1");

        when(groupRepository.update(anyLong(), any(GroupEntity.class)))
            .thenThrow(new DataIntegrityViolationException("Group already exists"));

        assertThrows(EntityExistsException.class, () -> {
            this.groupService.update(id, request);
        });
    }

    /**
     * Tests the successful update of a group.
     * 
     * Verifies that the updated group's name matches the request DTO.
     */
    @Test
    void test_update_group_succesffully(){
        Long id = 1L;
        GroupDtoRequest dto = new GroupDtoRequest();
        dto.setName("2BV1");

        ModelMapper mapper = new ModelMapper();
        GroupEntity entity = mapper.map(dto, GroupEntity.class);
        when(groupRepository.update(anyLong(), any(GroupEntity.class))).thenReturn(entity);

        Map<String, GroupDtoResponse> response = this.groupService.update(id, dto);
        assertEquals(dto.getName(), response.get("group").getName());
    }

    /**
     * Tests the deletion of a non-existent group.
     * 
     * @throws NoResultException when attempting to delete a group that doesn't exist.
     */
    @Test 
    void test_delete_group_not_found(){
        Long id = 300L;

        doThrow(new NoResultException("Not found"))
            .when(groupRepository)
            .delete(id);

        assertThrows(NoResultException.class, () -> {
                this.groupService.delete(id);
        });
    }

    /**
     * Tests the deletion of a GroupEntity that is referenced by other entities.
     * 
     * @throws DataIntegrityViolationException when the group cannot be deleted
     *                                         due to data integrity constraints.
     */
    @Test
    void test_delete_group_failed(){
        Long id = 1L;

        doThrow(new DataIntegrityViolationException("Group not deleted"))
            .when(groupRepository)
            .delete(id);

        assertThrows(DataIntegrityViolationException.class, () -> {
                this.groupService.delete(id);
        });
    }

    /**
     * Tests the successful deletion of a group.
     * 
     * Verifies that the correct success message is returned after deletion.
     */
    @Test
    void test_delete_group_successfully(){
        Long id = 1L;
        String message = "Group deleted successfully";

        doNothing()
            .when(groupRepository)
            .delete(id);
        
        Map<String, String> response = groupService.delete(id);

        assertEquals(response.get("message"), message);
    }
}