package org.widget.service;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.widget.EntityCreator;
import org.widget.entity.WidgetEntity;
import org.widget.exception.RequiredEntityNotFoundException;
import org.widget.internal.models.WidgetSearchRequestDto;
import org.widget.repository.WidgetRepositoryService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(profiles = "memory")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WidgetServiceTest {

    @Autowired
    private WidgetService widgetService;

    @Autowired
    private EntityCreator entityCreator;

    @MockBean
    private WidgetRepositoryService repository;

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

        Mockito.when(repository.save(Mockito.any(WidgetEntity.class))).then(AdditionalAnswers.returnsFirstArg());

        var widget = widgetService.create(dto);

        assertEquals(dto.getX(), widget.getX());
        assertEquals(dto.getY(), widget.getY());
        assertEquals(dto.getZ(), widget.getZ());
        assertEquals(dto.getWidth(), widget.getWidth());
        assertEquals(dto.getHeight(), widget.getHeight());
    }

    @Test
    void update() {
        var id = RandomUtils.nextLong();
        var dto = entityCreator.getTestDto();

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(new WidgetEntity().setId(id)));
        Mockito.when(repository.save(Mockito.any(WidgetEntity.class))).then(AdditionalAnswers.returnsFirstArg());

        var widget = widgetService.update(id, dto);

        assertEquals(dto.getX(), widget.getX());
        assertEquals(dto.getY(), widget.getY());
        assertEquals(dto.getZ(), widget.getZ());
        assertEquals(dto.getWidth(), widget.getWidth());
        assertEquals(dto.getHeight(), widget.getHeight());
    }

    @Test
    void updateWidgetNotFound() {
        var id = RandomUtils.nextLong();
        var dto = entityCreator.getTestDto();

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RequiredEntityNotFoundException.class, () -> widgetService.update(id, dto));
    }

    @Test
    void updateNullId() {
        var dto = entityCreator.getTestDto();
        assertThrows(NullPointerException.class, () -> widgetService.update(null, dto));
    }

    @Test
    void deleteNull() {
        assertThrows(NullPointerException.class, () -> widgetService.delete(null));
    }

    @Test
    void delete() {
        var id = RandomUtils.nextLong();
        assertDoesNotThrow(() -> widgetService.delete(id));
    }

    @Test
    void search() {
        var filter = new WidgetSearchRequestDto();
        assertDoesNotThrow(() -> widgetService.search(filter));
    }

    @Test
    void searchFilter() {
        var filter = entityCreator.getFilter();
        assertDoesNotThrow(() -> widgetService.search(filter));
    }
}