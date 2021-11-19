package org.widget.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.widget.entity.WidgetEntity;
import org.widget.internal.models.WidgetSearchRequestDto;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface WidgetRepositoryService {

    List<WidgetEntity> findAll();

    Page<WidgetEntity> findAll(@NotNull Pageable pageable);

    Optional<WidgetEntity> findById(@NotNull Long widgetId);

    WidgetEntity save(@NotNull WidgetEntity widget);

    void delete(@NotNull Long widgetId);

    Page<WidgetEntity> findByFilter(@NotNull WidgetSearchRequestDto filter, @NotNull Pageable pageable);
}
