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

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class WidgetService {

    private final WidgetRepository repository;

    @Transactional(readOnly = true)
    public WidgetEntity findById(@NotNull Long widgetId) {
        Objects.requireNonNull(widgetId);

        return repository.findById(widgetId)
                .orElseThrow(() -> new RequiredEntityNotFoundException("Widget " + widgetId + " not found"));
    }

    public WidgetEntity create(CreateWidgetDto dto) {
        Objects.requireNonNull(dto.getZ());

        if (repository.existsByZ(dto.getZ())) {
            throw new BadRequestException("Widget with Z: " + dto.getZ() + " already exists");
        }

        return updateEntity(dto, new WidgetEntity());
    }

    public WidgetEntity update(@NotNull Long widgetId, CreateWidgetDto dto) {
        Objects.requireNonNull(widgetId);
        Objects.requireNonNull(dto.getZ());

        if (repository.existsByZAndIdNot(dto.getZ(), widgetId)) {
            throw new BadRequestException("Widget with Z: " + dto.getZ() + " already exists");
        }

        return updateEntity(dto, findById(widgetId));
    }

    @Transactional
    WidgetEntity updateEntity(@NotNull CreateWidgetDto dto, @NotNull WidgetEntity widget) {
        widget.setX(dto.getX())
                .setY(dto.getY())
                .setZ(dto.getZ())
                .setWidth(dto.getWidth())
                .setHeight(dto.getHeight());

        return repository.save(widget);
    }

    @Transactional
    public void delete(Long widgetId) {
        if (repository.existsById(widgetId)) {
            repository.deleteById(widgetId);
        }
    }
}
