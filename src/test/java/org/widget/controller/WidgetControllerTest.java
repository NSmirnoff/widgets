package org.widget.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.widget.EntityCreator;
import org.widget.service.WidgetService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WidgetControllerTest {

    @Autowired
    private WidgetController controller;

    @Autowired
    private EntityCreator entityCreator;

    @Test
    void createWidget() {
        var dto = entityCreator.getTestDto();
        var entityDto = controller.createWidget(dto);

        assertNotNull(entityDto.getBody());
        assertEquals(dto.getX(), entityDto.getBody().getX());
        assertEquals(dto.getY(), entityDto.getBody().getY());
        assertEquals(dto.getZ(), entityDto.getBody().getZ());
        assertEquals(dto.getWidth(), entityDto.getBody().getWidth());
        assertEquals(dto.getHeight(), entityDto.getBody().getHeight());
    }

    @Test
    void getWidget() {
        var entity = entityCreator.getTestEntity();
        var entityDto = controller.getWidget(entity.getId());

        assertNotNull(entityDto.getBody());
        assertEquals(entity.getId(), entityDto.getBody().getId());
    }

    @Test
    void updateWidget() {
        var dto = entityCreator.getTestDto();
        var entity = entityCreator.getTestEntity();

        var entityDto = controller.updateWidget(entity.getId(), dto);

        assertNotNull(entityDto.getBody());
        assertEquals(dto.getX(), entityDto.getBody().getX());
        assertEquals(dto.getY(), entityDto.getBody().getY());
        assertEquals(dto.getZ(), entityDto.getBody().getZ());
        assertEquals(dto.getWidth(), entityDto.getBody().getWidth());
        assertEquals(dto.getHeight(), entityDto.getBody().getHeight());
    }

    @Test
    void deleteWidget() {
        var entity = entityCreator.getTestEntity();
        var id = entity.getId();
        controller.deleteWidget(id);

        assertEquals(controller.getWidget(id).getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void listWidgets() {

    }
}