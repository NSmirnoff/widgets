package org.widget.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.widget.entity.WidgetEntity;
import org.widget.exception.RequiredEntityNotFoundException;
import org.widget.internal.models.CreateWidgetDto;
import org.widget.internal.models.WidgetSearchRequestDto;
import org.widget.repository.WidgetRepositoryService;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class WidgetService {

    private final WidgetRepositoryService repository;

    public WidgetEntity findById(@NotNull Long widgetId) {
        Objects.requireNonNull(widgetId);
        return repository.findById(widgetId)
                .orElseThrow(() -> new RequiredEntityNotFoundException("Widget " + widgetId + " not found"));
    }

    public WidgetEntity create(@NotNull CreateWidgetDto dto) {
        Objects.requireNonNull(dto);
        return updateEntity(dto, new WidgetEntity());
    }

    @Transactional
    public WidgetEntity update(@NotNull Long widgetId, @NotNull CreateWidgetDto dto) {
        Objects.requireNonNull(widgetId);
        Objects.requireNonNull(dto);

        return updateEntity(dto, findById(widgetId));
    }

    private WidgetEntity updateEntity(@NotNull CreateWidgetDto dto, @NotNull WidgetEntity widget) {
        Objects.requireNonNull(dto.getX());
        Objects.requireNonNull(dto.getY());
        Objects.requireNonNull(dto.getWidth());
        Objects.requireNonNull(dto.getHeight());

        widget.setX(dto.getX())
                .setY(dto.getY())
                .setZ(dto.getZ())
                .setWidth(dto.getWidth())
                .setHeight(dto.getHeight())
                .setLastUpdated(LocalDateTime.now());

        return repository.save(widget);
    }

    @Transactional
    public void delete(@NotNull Long widgetId) {
        Objects.requireNonNull(widgetId);
        repository.delete(widgetId);
    }

    public Page<WidgetEntity> search(@NotNull WidgetSearchRequestDto filter) {
        Objects.requireNonNull(filter);
        var pageable = PageRequest.of(filter.getPage() - 1, filter.getSize());
        if (filter.getxMin() != null && filter.getxMax() != null &&
                filter.getyMin() != null && filter.getyMax() != null) {
            return repository.findByFilter(filter, pageable);
        } else {
            return repository.findAll(pageable);
        }
    }
}
