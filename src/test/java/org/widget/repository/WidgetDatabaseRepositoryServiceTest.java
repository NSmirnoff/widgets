package org.widget.repository;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.widget.EntityCreator;
import org.widget.entity.WidgetEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(profiles = "database")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WidgetDatabaseRepositoryServiceTest {

    @Autowired
    private WidgetDatabaseRepositoryService service;

    @Autowired
    private EntityCreator entityCreator;

    @MockBean
    private WidgetRepository repository;

    @Test
    void findAll() {
        var widget = new WidgetEntity();
        Mockito.when(repository.findAll()).thenReturn(List.of(widget));
        assertTrue(service.findAll().size() > 0);
    }

    @Test
    void findAllPageable() {
        var widget = new WidgetEntity();
        var pageable = PageRequest.of(0, 10);
        Mockito.when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(widget)));
        assertTrue(service.findAll(pageable).getContent().size() > 0);
    }

    @Test
    void findById() {
        var id = RandomUtils.nextLong();
        var widget = new WidgetEntity();
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(widget));
        assertTrue(service.findById(id).isPresent());
    }

    @Test
    void create() {
        var widget = new WidgetEntity().setZ(RandomUtils.nextInt());
        Mockito.when(repository.findByZ(widget.getZ())).thenReturn(Optional.empty());
        Mockito.when(repository.save(widget)).thenReturn(widget);

        assertDoesNotThrow(() -> service.save(widget));
    }

    @Test
    void createNullZ() {
        var widget = new WidgetEntity();
        Mockito.when(repository.findMaxZ(null)).thenReturn(RandomUtils.nextInt());
        Mockito.when(repository.save(widget)).thenReturn(widget);

        assertDoesNotThrow(() -> service.save(widget));
    }

    @Test
    void update() {
        var widget = new WidgetEntity()
                .setId(RandomUtils.nextLong())
                .setZ(RandomUtils.nextInt());
        Mockito.when(repository.findByZ(widget.getZ())).thenReturn(Optional.empty());
        Mockito.when(repository.save(widget)).thenReturn(widget);

        assertDoesNotThrow(() -> service.save(widget));
    }

    @Test
    void updateNullZ() {
        var widget = new WidgetEntity().setId(RandomUtils.nextLong());
        Mockito.when(repository.findMaxZ(widget.getId())).thenReturn(RandomUtils.nextInt());
        Mockito.when(repository.save(widget)).thenReturn(widget);

        assertDoesNotThrow(() -> service.save(widget));
    }

    @Test
    void delete() {
        var id = RandomUtils.nextLong();
        Mockito.when(repository.existsById(id)).thenReturn(true);
        assertDoesNotThrow(() -> service.delete(id));
    }

    @Test
    void findByFilter() {
        var pageable = PageRequest.of(0, 10);
        var filter = entityCreator.getFilter();
        Mockito.when(
                repository.findByExpression(
                        filter.getxMin().doubleValue(),
                        filter.getxMax().doubleValue(),
                        filter.getyMin().doubleValue(),
                        filter.getyMax().doubleValue(),
                        pageable
                )
        ).thenReturn(new PageImpl<>(List.of(new WidgetEntity())));

        assertTrue(service.findByFilter(filter, pageable).getContent().size() > 0);
    }
}