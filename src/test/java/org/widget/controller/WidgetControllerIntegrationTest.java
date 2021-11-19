package org.widget.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.widget.EntityCreator;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(profiles = "memory")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WidgetControllerIntegrationTest {

    @Autowired
    private WidgetController controller;

    @Autowired
    private EntityCreator entityCreator;

    @Test
    void createWidget() {
        var dto = entityCreator.getTestDto();
        var widgetDto = controller.createWidget(dto);

        assertNotNull(widgetDto.getBody());
        assertEquals(dto.getX(), widgetDto.getBody().getX());
        assertEquals(dto.getY(), widgetDto.getBody().getY());
        assertEquals(dto.getZ(), widgetDto.getBody().getZ());
        assertEquals(dto.getWidth(), widgetDto.getBody().getWidth());
        assertEquals(dto.getHeight(), widgetDto.getBody().getHeight());
    }

    @Test
    void getWidget() {
        var widget = entityCreator.getTestEntity();
        var widgetDto = controller.getWidget(widget.getId());

        assertNotNull(widgetDto.getBody());
        assertEquals(widget.getId(), widgetDto.getBody().getId());
    }

    @Test
    void updateWidget() {
        var dto = entityCreator.getTestDto();
        var widget = entityCreator.getTestEntity();

        var widgetDto = controller.updateWidget(widget.getId(), dto);

        assertNotNull(widgetDto.getBody());
        assertEquals(dto.getX(), widgetDto.getBody().getX());
        assertEquals(dto.getY(), widgetDto.getBody().getY());
        assertEquals(dto.getZ(), widgetDto.getBody().getZ());
        assertEquals(dto.getWidth(), widgetDto.getBody().getWidth());
        assertEquals(dto.getHeight(), widgetDto.getBody().getHeight());
    }

    @Test
    void deleteWidget() {
        var widget = entityCreator.getTestEntity();
        var id = widget.getId();
        controller.deleteWidget(id);

        assertEquals(controller.getWidget(id).getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void listWidgets() {
        var filter = entityCreator.getFilter();
        var response = controller.listWidgets(filter);

        assertNotNull(response.getBody());
        var page = response.getBody();

        assertTrue(page.getContent().size() > 0);
    }
}