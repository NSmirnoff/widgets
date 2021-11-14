package org.widget.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.widget.EntityCreator;
import org.widget.entity.WidgetEntity;
import org.widget.repository.WidgetRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WidgetSearchServiceTest {

    @Autowired
    private WidgetSearchService service;

    @Autowired
    private EntityCreator entityCreator;

    @MockBean
    private WidgetRepository repository;

    @Test
    void searchNullPage() {
        var filter = entityCreator.getFilter();
        filter.setPage(null);
        assertThrows(NullPointerException.class, () -> service.search(filter));
        filter.setPage(1);
    }

    @Test
    void searchNullSize() {
        var filter = entityCreator.getFilter();
        filter.setSize(null);
        assertThrows(NullPointerException.class, () -> service.search(filter));
        filter.setSize(10);
    }

    @Test
    void searchNullFilterXMin() {
        var filter = entityCreator.getFilter();
        filter.setxMin(null);
        assertThrows(NullPointerException.class, () -> service.search(filter));
        filter.setxMin(Integer.MIN_VALUE);
    }

    @Test
    void searchNullFilterXMax() {
        var filter = entityCreator.getFilter();
        filter.setxMax(null);
        assertThrows(NullPointerException.class, () -> service.search(filter));
        filter.setxMax(Integer.MAX_VALUE);
    }

    @Test
    void searchNullFilterYMin() {
        var filter = entityCreator.getFilter();
        filter.setyMin(null);
        assertThrows(NullPointerException.class, () -> service.search(filter));
        filter.setyMin(Integer.MIN_VALUE);
    }

    @Test
    void searchNullFilterYMax() {
        var filter = entityCreator.getFilter();
        filter.setyMax(null);
        assertThrows(NullPointerException.class, () -> service.search(filter));
        filter.setyMax(Integer.MAX_VALUE);
    }

    @Test
    void search() {
        var filter = entityCreator.getFilter();

        var pageable = PageRequest.of(0, 10);
        Page<WidgetEntity> pageMock = new PageImpl<>(List.of(new WidgetEntity()));
        Mockito.when(
                repository.findByExpression(
                        filter.getxMin().doubleValue(), filter.getxMax().doubleValue(),
                        filter.getyMin().doubleValue(), filter.getxMax().doubleValue(),
                        pageable
                )
        ).thenReturn(pageMock);

        assertDoesNotThrow(() -> service.search(filter));
    }
}