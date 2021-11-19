package org.widget.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.widget.exception.BadRequestException;
import org.widget.exception.RequiredEntityNotFoundException;
import org.widget.internal.controllers.InternalWidgetApiDelegate;
import org.widget.internal.models.CreateWidgetDto;
import org.widget.internal.models.WidgetDto;
import org.widget.internal.models.WidgetSearchRequestDto;
import org.widget.internal.models.WidgetSearchResponseDto;
import org.widget.mapper.WidgetMapper;
import org.widget.service.WidgetService;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
public class WidgetController implements InternalWidgetApiDelegate {

    private final WidgetService service;
    private final WidgetMapper mapper;

    @Override
    public ResponseEntity<WidgetDto> createWidget(@NotNull CreateWidgetDto dto) {
        var widget = service.create(dto);
        return ResponseEntity.ok(mapper.toDto(widget));
    }

    @Override
    public ResponseEntity<WidgetDto> getWidget(@NotNull Long widgetId) {
        try {
            var widget = service.findById(widgetId);
            return ResponseEntity.ok(mapper.toDto(widget));
        } catch (RequiredEntityNotFoundException ignore) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<WidgetDto> updateWidget(@NotNull Long widgetId, @NotNull CreateWidgetDto dto) {
        var widget = service.update(widgetId, dto);
        return ResponseEntity.ok(mapper.toDto(widget));
    }

    @Override
    public ResponseEntity<Void> deleteWidget(@NotNull Long widgetId) {
        service.delete(widgetId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search widgets in area (xMin;yMin) - (xMax;yMax) or without it
     * @param filter - xMin, xMax, yMin, yMax, page and size of page
     * @return Custom page dto with widgets sort by Z
     */
    @Override
    public ResponseEntity<WidgetSearchResponseDto> listWidgets(@NotNull WidgetSearchRequestDto filter) {
        filter = filter != null ? filter : new WidgetSearchRequestDto();
        if (filter.getxMin() != null && filter.getxMax() != null &&
                filter.getyMin() != null && filter.getyMax() != null) {
            if (filter.getxMin().equals(filter.getxMax()) || filter.getyMin().equals(filter.getyMax())) {
                throw new BadRequestException("Width or height of searching window is 0");
            }

            if (filter.getxMin() > filter.getxMax()) {
                throw new BadRequestException("X0 more than X1");
            }

            if (filter.getyMin() > filter.getyMax()) {
                throw new BadRequestException("Y0 more than Y1");
            }
        }

        var page = service.search(filter);
        return ResponseEntity.ok(mapper.toDto(page));
    }
}
