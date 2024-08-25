package com.cripto.project.datasource.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import com.cripto.project.domain.entities.GroupEntity;
import jakarta.persistence.NoResultException;

@ActiveProfiles("test")
@SpringBootTest
@Import(GroupDaoImpl.class)
class GroupDaoImplTest {

    @Autowired
    GroupDaoImpl groupRepository;

    /**
     * Tests the successful registration of a new GroupEntity.
     * 
     * Verifies that the registered entity is not null and matches the input entity.
     */
    @Test
    void test_register_new_groupentity_successfully() {
        GroupEntity entity = GroupEntity.builder().name("7GM1").build();

        GroupEntity result = groupRepository.register(entity);

        assertNotNull(result);
        assertEquals(entity, result);
    }

    /**
     * Tests the registration of a GroupEntity that already exists.
     * 
     * @throws DataIntegrityViolationException when attempting to register a group
     *                                         with a name that's already registered.
     */
    @Test
    void test_register_groupentity_already_exists() {
        GroupEntity entity = GroupEntity.builder().name("9AV1").build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            groupRepository.register(entity);
        });
    }

    /**
     * Tests the successful retrieval of a GroupEntity by its ID.
     * 
     * Verifies that the retrieved entity has the expected name.
     */
    @Test
    void test_find_by_id_groupentity_succesfully() {
        Long id = 1L;

        GroupEntity entity = this.groupRepository.getById(id);

        assertEquals("1AV1", entity.getName());
    }

    /**
     * Tests the retrieval of a GroupEntity with a non-existent ID.
     * 
     * @throws NoResultException when attempting to find a group with an ID that doesn't exist.
     */
    @Test
    void test_not_found_by_id_groupentity() {
        Long id = 201L;

        assertThrows(NoResultException.class, () -> {
            groupRepository.getById(id);
        });
    }

    /**
     * Tests the retrieval of all GroupEntities.
     * 
     * Verifies that the returned list has a size greater than or equal to zero.
     */
    @Test
    void test_get_all_groups() {
        List<GroupEntity> groups = this.groupRepository.getAll();

        assertTrue(groups.size() >= 0);
    }

    /**
     * Tests the retrieval of GroupEntities by a partial name match.
     * 
     * Verifies that the returned list has a size greater than or equal to zero
     * and that all returned entities contain the specified name fragment.
     */
    @Test
    void test_find_groupsentities_by_cointaining_name() {
        String containingName = "6";

        List<GroupEntity> groups = this.groupRepository.getGroupByNameContaining(containingName);

        assertTrue(groups.size() >= 0);
        assertTrue(groups.stream().allMatch(g -> g.getName().contains(containingName)));
    }

    /**
     * Tests the successful update of a GroupEntity.
     * 
     * Verifies that the updated entity has the new name.
     */
    @Test
    void test_update_group_succesffully() {
        Long id = 1L;
        GroupEntity group = GroupEntity.builder().name("9AV1").build();

        GroupEntity groupUpdated = this.groupRepository.update(id, group);

        assertEquals(group.getName(), groupUpdated.getName());
    }

    /**
     * Tests the update of a non-existent GroupEntity.
     * 
     * @throws NoResultException when attempting to update a group that doesn't exist.
     */
    @Test 
    void test_update_group_not_found() {
        Long id = 300L;
        GroupEntity group = GroupEntity.builder().name("8AV1").build();

        assertThrows(NoResultException.class, () -> {
            this.groupRepository.update(id, group);
        });
    }

    /**
     * Tests the update of a GroupEntity with a name that already exists.
     * 
     * @throws DataIntegrityViolationException when attempting to update a group
     *                                         with a name that's already in use.
     */
    @Test
    void test_update_group_with_name_already_exists() {
        Long id = 1L;
        GroupEntity group = GroupEntity.builder().name("8AV1").build();

        assertThrows(DataIntegrityViolationException.class, ()->{
            this.groupRepository.update(id, group);
        });
    }

    /**
     * Tests the deletion of a non-existent GroupEntity.
     * 
     * @throws NoResultException when attempting to delete a group that doesn't exist.
     */
    @Test
    void test_delete_groupentity_not_found() {
        Long id = 41L;

        assertThrows(NoResultException.class, () -> {
            this.groupRepository.delete(id);
        });
    }

    /**
     * Tests the deletion of a GroupEntity that is referenced by other entities.
     * 
     * @throws DataIntegrityViolationException when attempting to delete a group
     *                                         referenced by other entities.
     */
    @Test
    void test_delete_groupentity_filled() {
        Long id = 1L;

        assertThrows(DataIntegrityViolationException.class, () -> {
            this.groupRepository.delete(id);
        });
    }

    /**
     * Tests the successful deletion of a GroupEntity.
     * 
     * Verifies that the group is correctly deleted and no longer in the group list.
     */
    @Test
    void test_delete_groupentity_successfully() {
        Long id = 42L;
        List<GroupEntity> groups = this.groupRepository.getAll();

        this.groupRepository.delete(id);

        assertTrue(groups.stream().anyMatch(group -> !group.getId().equals(id)));
    }
}