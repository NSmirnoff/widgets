package org.widget.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.widget.internal.controllers.InternalWidgetApiDelegate;
import org.widget.internal.models.CreateWidgetDto;
import org.widget.internal.models.WidgetDto;
import org.widget.internal.models.WidgetJournalRequestDto;
import org.widget.internal.models.WidgetJournalResponseDto;
import org.widget.mapper.WidgetMapper;
import org.widget.service.WidgetService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WidgetController implements InternalWidgetApiDelegate {

    private final WidgetService service;
    private final WidgetMapper mapper;

    @Override
    public ResponseEntity<WidgetDto> createWidget(CreateWidgetDto dto) {
        var widget = service.create(dto);
        return ResponseEntity.ok(mapper.toDto(widget));
    }

    @Override
    public ResponseEntity<WidgetDto> getWidget(Long widgetId) {
        var widget = service.findById(widgetId);
        return ResponseEntity.ok(mapper.toDto(widget));
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
    public ResponseEntity<WidgetJournalResponseDto> listWidgets(WidgetJournalRequestDto filter) {
        return ResponseEntity.ok().build();
    }
}
