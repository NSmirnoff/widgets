package org.widget.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.widget.EntityCreator;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(profiles = "memory")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WidgetMemoryRepositoryServiceTest {

    @Autowired
    private WidgetMemoryRepositoryService service;

    @Autowired
    private EntityCreator entityCreator;

    @Test
    void findAll() {
        entityCreator.getTestEntity();
        assertTrue(service.findAll().size() > 0);
    }

    @Test
    void findAllPageable() {
        var pageable = PageRequest.of(0, 10);
        entityCreator.getTestEntity();
        assertTrue(service.findAll(pageable).getContent().size() > 0);
    }

    @Test
    void findById() {
        var widget = entityCreator.getTestEntity();
        assertTrue(service.findById(widget.getId()).isPresent());
    }

    @Test
    void saveNew() {
        var widget = entityCreator.getNewTestEntity();
        widget = service.save(widget);
        assertNotNull(widget.getId());
    }

    @Test
    void saveNullZ() {
        var widget = entityCreator.getNewTestEntity().setZ(null);
        widget = service.save(widget);
        assertNotNull(widget.getZ());
    }

    @Test
    void saveExistsZ() {
        var widget = entityCreator.getTestEntity();
        var z = widget.getZ();
        var newWidget = entityCreator.getNewTestEntity().setZ(z);
        newWidget = service.save(newWidget);
        assertEquals(z, newWidget.getZ());
    }

    @Test
    void delete() {
        var widget = entityCreator.getTestEntity();
        service.delete(widget.getId());

        assertFalse(service.findById(widget.getId()).isPresent());
    }

    @Test
    void findByFilter() {
        var pageable = PageRequest.of(0, 10);
        var filter = entityCreator.getFilter();
        entityCreator.getTestEntity();

        assertTrue(service.findByFilter(filter, pageable).getContent().size() > 0);
    }
}