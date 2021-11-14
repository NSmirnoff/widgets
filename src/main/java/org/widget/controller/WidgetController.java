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
import org.widget.service.WidgetSearchService;
import org.widget.service.WidgetService;

@RestController
@RequiredArgsConstructor
public class WidgetController implements InternalWidgetApiDelegate {

    private final WidgetService service;
    private final WidgetSearchService searchService;
    private final WidgetMapper mapper;

    @Override
    public ResponseEntity<WidgetDto> createWidget(CreateWidgetDto dto) {
        var widget = service.create(dto);
        return ResponseEntity.ok(mapper.toDto(widget));
    }

    @Override
    public ResponseEntity<WidgetDto> getWidget(Long widgetId) {
        try {
            var widget = service.findById(widgetId);
            return ResponseEntity.ok(mapper.toDto(widget));
        } catch (RequiredEntityNotFoundException ignore) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<WidgetDto> updateWidget(Long widgetId, CreateWidgetDto dto) {
        var widget = service.update(widgetId, dto);
        return ResponseEntity.ok(mapper.toDto(widget));
    }

    @Override
    public ResponseEntity<Void> deleteWidget(Long widgetId) {
        service.delete(widgetId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<WidgetSearchResponseDto> listWidgets(WidgetSearchRequestDto filter) {
        if (filter.getxMin().equals(filter.getxMax()) || filter.getyMin().equals(filter.getyMax())) {
            throw new BadRequestException("Width or height of searching window is 0");
        }

        if (filter.getxMin() > filter.getxMax()) {
            filter.setxMin(filter.getxMin() ^ filter.getxMax());
            filter.setxMax(filter.getxMax() ^ filter.getxMin());
            filter.setxMin(filter.getxMin() ^ filter.getxMax());
        }

        if (filter.getyMin() > filter.getyMax()) {
            filter.setyMin(filter.getyMin() ^ filter.getyMax());
            filter.setyMax(filter.getyMax() ^ filter.getyMin());
            filter.setyMin(filter.getyMin() ^ filter.getyMax());
        }

        var page = searchService.search(filter);
        return ResponseEntity.ok(mapper.toDto(page));
    }
}
