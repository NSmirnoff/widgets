package org.widget.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.widget.entity.WidgetEntity;
import org.widget.internal.models.WidgetSearchRequestDto;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Profile("memory")
@Slf4j
@Service
public class WidgetMemoryRepositoryService implements WidgetRepositoryService {

    private Set<WidgetEntity> entities = new LinkedHashSet<>();
    private Long idSequence = 1L;

    @Override
    @Transactional(readOnly = true)
    public List<WidgetEntity> findAll() {
        return new ArrayList<>(entities);
    }

    @Override
    public Page<WidgetEntity> findAll(@NotNull Pageable pageable) {
        Objects.requireNonNull(pageable);

        var offset = (pageable.getPageNumber()) * pageable.getPageSize();
        var pageContent = entities.stream()
                .skip(offset)
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        return new PageImpl<>(pageContent, pageable, entities.size());
    }

    @Override
    public Optional<WidgetEntity> findById(@NotNull Long widgetId) {
        Objects.requireNonNull(widgetId);

        return entities.stream()
                .filter(e -> e.getId().equals(widgetId))
                .findFirst();
    }

    @Override
    public synchronized WidgetEntity save(WidgetEntity widget) {
        Objects.requireNonNull(widget);

        if (widget.getZ() == null) {
            widget.setZ(findMaxZ() + 1);
        } else {
            shiftByZIndex(widget.getZ(), widget.getId());
        }

        if (widget.getId() == null) {
            widget.setId(idSequence++);
            entities.add(widget);
        }

        entities = entities.stream()
                .sorted(Comparator.comparing(WidgetEntity::getZ))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return widget;
    }

    private Integer findMaxZ() {
        return entities.stream()
                .map(WidgetEntity::getZ)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(Integer.MIN_VALUE);
    }

    private void shiftByZIndex(@NotNull Integer newZ, Long widgetId) {
        Objects.requireNonNull(newZ);

        var sameZWidgetOptional = entities.stream()
                .filter(w -> w.getZ().equals(newZ) && !w.getId().equals(widgetId))
                .findFirst();

        while (sameZWidgetOptional.isPresent()) {
            var widget = sameZWidgetOptional.get();
            widget.setZ(widget.getZ() + 1);

            sameZWidgetOptional = entities.stream()
                    .filter(w -> w.getZ().equals(widget.getZ()) && !w.getId().equals(widget.getId()))
                    .findFirst();
        }
    }

    @Override
    public synchronized void delete(@NotNull Long widgetId) {
        Objects.requireNonNull(widgetId);
        entities.removeIf(e -> e.getId().equals(widgetId));
    }

    @Override
    public Page<WidgetEntity> findByFilter(@NotNull WidgetSearchRequestDto filter, @NotNull Pageable pageable) {
        Objects.requireNonNull(filter);
        Objects.requireNonNull(pageable);

        var entitiesStream = entities
                .stream()
                .filter(w -> WidgetMemoryServiceUtil.widgetInWindow(w, filter));

        var list = entitiesStream.collect(Collectors.toList());
        var total = list.size();
        entitiesStream = list.stream();

        var offset = (filter.getPage() - 1) * filter.getSize();
        var pageContent = entitiesStream
                .skip(offset)
                .limit(filter.getSize())
                .collect(Collectors.toList());

        return new PageImpl<>(pageContent, pageable, total);
    }
}
