package org.widget.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.widget.entity.WidgetEntity;
import org.widget.exception.BadRequestException;
import org.widget.exception.RequiredEntityNotFoundException;
import org.widget.internal.models.CreateWidgetDto;
import org.widget.repository.WidgetRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class WidgetService {

    private final WidgetRepository repository;

    @Transactional(readOnly = true)
    public WidgetEntity findById(Long widgetId) {
        return repository.findById(widgetId)
                .orElseThrow(() -> new RequiredEntityNotFoundException("Widget " + widgetId + " not found"));
    }

    public WidgetEntity create(CreateWidgetDto dto) {
        if (repository.existsByZ(dto.getZ())) {
            throw new BadRequestException("Widget with Z: " + dto.getZ() + " already exists");
        }

        return update(new WidgetEntity(), dto);
    }

    public WidgetEntity update(Long widgetId, CreateWidgetDto dto) {
        var widget = findById(widgetId);
        if (repository.existsByZAndIdNot(dto.getZ(), widgetId)) {
            throw new BadRequestException("Widget with Z: " + dto.getZ() + " already exists");
        }

        return update(widget, dto);
    }

    @Transactional
    WidgetEntity update(WidgetEntity entity, CreateWidgetDto dto) {
        entity.setX(dto.getX())
                .setY(dto.getX())
                .setZ(dto.getZ())
                .setWidth(dto.getWidth())
                .setHeight(dto.getHeight());

        return repository.save(entity);
    }

    @Transactional
    public void delete(Long widgetId) {
        var widget = findById(widgetId);
        repository.delete(widget);
    }
}
