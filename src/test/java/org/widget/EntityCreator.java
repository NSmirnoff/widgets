package org.widget;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.widget.entity.WidgetEntity;
import org.widget.internal.models.CreateWidgetDto;
import org.widget.internal.models.WidgetSearchRequestDto;
import org.widget.repository.WidgetRepository;

import java.time.LocalDateTime;
import java.util.Iterator;

@Component
public class EntityCreator {

    @Autowired
    private WidgetRepository repository;

    private CreateWidgetDto dto;
    private WidgetSearchRequestDto searchDto;

    public CreateWidgetDto getTestDto() {
        if (this.dto == null) {
            this.dto = new CreateWidgetDto()
                    .width(RandomUtils.nextInt(0, Integer.MAX_VALUE))
                    .height(RandomUtils.nextInt(0, Integer.MAX_VALUE))
                    .x(RandomUtils.nextInt())
                    .y(RandomUtils.nextInt())
                    .z(RandomUtils.nextInt());
        }

        return this.dto;
    }

    public WidgetSearchRequestDto getFilter() {
        if (this.searchDto == null) {
            this.searchDto = new WidgetSearchRequestDto()
                    .xMin(Integer.MIN_VALUE)
                    .xMax(Integer.MAX_VALUE)
                    .yMin(Integer.MIN_VALUE)
                    .yMax(Integer.MAX_VALUE);
        }

        return this.searchDto;
    }

    public WidgetEntity getTestEntity() {
        var entity = findFirst(repository);
        if (entity == null) {
            entity = repository.save(new WidgetEntity()
                    .setWidth(RandomUtils.nextInt(0, Integer.MAX_VALUE))
                    .setHeight(RandomUtils.nextInt(0, Integer.MAX_VALUE))
                    .setLastUpdated(LocalDateTime.now())
                    .setX(RandomUtils.nextInt())
                    .setY(RandomUtils.nextInt())
                    .setZ(RandomUtils.nextInt()));
        }

        return entity;
    }

    private static <T> T findFirst(CrudRepository<T, Long> repository) {
        Iterator<T> iterator = repository.findAll().iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }
}
