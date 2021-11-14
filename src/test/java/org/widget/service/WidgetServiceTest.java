package org.widget.service;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.widget.EntityCreator;
import org.widget.entity.WidgetEntity;
import org.widget.exception.BadRequestException;
import org.widget.exception.RequiredEntityNotFoundException;
import org.widget.repository.WidgetRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WidgetServiceTest {

    @Autowired
    private WidgetService widgetService;

    @Autowired
    private EntityCreator entityCreator;

    @MockBean
    private WidgetRepository repository;

    @Test
    void findById() {
        var id = RandomUtils.nextLong();
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(new WidgetEntity()));
        assertDoesNotThrow(() -> widgetService.findById(id));
    }

    @Test
    void findByIdWidgetNotFound() {
        var id = RandomUtils.nextLong();
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(RequiredEntityNotFoundException.class, () -> widgetService.findById(id));
    }

    @Test
    void findByIdNullId() {
        assertThrows(NullPointerException.class, () -> widgetService.findById(null));
    }

    @Test
    void create() {
        var dto = entityCreator.getTestDto();
        Mockito.when(repository.existsByZ(dto.getZ())).thenReturn(false);
        assertDoesNotThrow(() -> widgetService.create(dto));
    }

    @Test
    void createWithExistsZ() {
        var dto = entityCreator.getTestDto();
        Mockito.when(repository.existsByZ(dto.getZ())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> widgetService.create(dto));
    }

    @Test
    void createNullZ() {
        var dto = entityCreator.getTestDto();
        dto.setZ(null);

        assertThrows(NullPointerException.class, () -> widgetService.create(dto));
    }

    @Test
    void update() {
        var id = RandomUtils.nextLong();
        var dto = entityCreator.getTestDto();

        Mockito.when(repository.existsByZAndIdNot(dto.getZ(), id)).thenReturn(false);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(new WidgetEntity()));

        assertDoesNotThrow(() -> widgetService.update(id, dto));
    }

    @Test
    void updateEntity() {
        var dto = entityCreator.getTestDto();
        var entity = new WidgetEntity();

        Mockito.when(repository.save(entity)).thenReturn(entity);
        entity = widgetService.updateEntity(dto, entity);

        assertEquals(dto.getX(), entity.getX());
        assertEquals(dto.getY(), entity.getY());
        assertEquals(dto.getZ(), entity.getZ());
        assertEquals(dto.getWidth(), entity.getWidth());
        assertEquals(dto.getHeight(), entity.getHeight());
    }

    @Test
    void updateWidgetNotFound() {
        var id = RandomUtils.nextLong();
        var dto = entityCreator.getTestDto();

        Mockito.when(repository.existsByZAndIdNot(dto.getZ(), id)).thenReturn(false);
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RequiredEntityNotFoundException.class, () -> widgetService.update(id, dto));
    }

    @Test
    void updateWithExistsZ() {
        var id = RandomUtils.nextLong();
        var dto = entityCreator.getTestDto();

        Mockito.when(repository.existsByZAndIdNot(dto.getZ(), id)).thenReturn(true);
        assertThrows(BadRequestException.class, () -> widgetService.update(id, dto));
    }

    @Test
    void updateNullZ() {
        var id = RandomUtils.nextLong();

        var dto = entityCreator.getTestDto();
        dto.setZ(null);

        assertThrows(NullPointerException.class, () -> widgetService.update(id, dto));
    }

    @Test
    void updateNullId() {
        var dto = entityCreator.getTestDto();
        assertThrows(NullPointerException.class, () -> widgetService.update(null, dto));
    }

    @Test
    void delete() {
        var id = RandomUtils.nextLong();

        Mockito.when(repository.existsById(id)).thenReturn(true);
        assertDoesNotThrow(() -> widgetService.delete(id));

        Mockito.when(repository.existsById(id)).thenReturn(false);
        assertDoesNotThrow(() -> widgetService.delete(id));
    }
}