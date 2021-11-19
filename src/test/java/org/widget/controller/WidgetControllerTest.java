package org.widget.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;
import org.widget.EntityCreator;
import org.widget.entity.WidgetEntity;
import org.widget.exception.BadRequestException;
import org.widget.service.WidgetService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles(profiles = "memory")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WidgetControllerTest {

    @Autowired
    private WidgetController controller;

    @Autowired
    private EntityCreator entityCreator;

    @MockBean
    private WidgetService service;

    @Test
    void equalXMinAndMax() {
        var filter = entityCreator.getFilter();
        filter.setxMin(filter.getxMax());

        Mockito.when(service.search(filter)).thenReturn(new PageImpl<>(List.of(new WidgetEntity())));
        assertThrows(BadRequestException.class, () -> controller.listWidgets(filter));

        filter.setxMin(Integer.MIN_VALUE);
    }

    @Test
    void wrongFilterX() {
        var filter = entityCreator.getFilter();
        filter.setxMin(Integer.MAX_VALUE);
        filter.setxMax(Integer.MIN_VALUE);

        Mockito.when(service.search(filter)).thenReturn(new PageImpl<>(List.of(new WidgetEntity())));
        assertThrows(BadRequestException.class, () -> controller.listWidgets(filter));

        filter.setxMin(Integer.MIN_VALUE);
        filter.setxMax(Integer.MAX_VALUE);
    }

    @Test
    void equalYMinAndMax() {
        var filter = entityCreator.getFilter();
        filter.setyMin(filter.getyMax());

        Mockito.when(service.search(filter)).thenReturn(new PageImpl<>(List.of(new WidgetEntity())));
        assertThrows(BadRequestException.class, () -> controller.listWidgets(filter));

        filter.setyMin(Integer.MIN_VALUE);
    }

    @Test
    void wrongFilterY() {
        var filter = entityCreator.getFilter();
        filter.setyMin(Integer.MAX_VALUE);
        filter.setyMax(Integer.MIN_VALUE);

        Mockito.when(service.search(filter)).thenReturn(new PageImpl<>(List.of(new WidgetEntity())));
        assertThrows(BadRequestException.class, () -> controller.listWidgets(filter));

        filter.setyMin(Integer.MIN_VALUE);
        filter.setyMax(Integer.MAX_VALUE);
    }
}
