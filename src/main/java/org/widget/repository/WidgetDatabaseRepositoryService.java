package org.widget.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.widget.entity.WidgetEntity;
import org.widget.internal.models.WidgetSearchRequestDto;

import javax.validation.constraints.NotNull;
import java.util.*;

@Profile("database")
@Slf4j
@Service
@RequiredArgsConstructor
public class WidgetDatabaseRepositoryService implements WidgetRepositoryService {

    private final WidgetRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<WidgetEntity> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WidgetEntity> findAll(@NotNull Pageable pageable) {
        Objects.requireNonNull(pageable);
        return repository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WidgetEntity> findById(@NotNull Long widgetId) {
        Objects.requireNonNull(widgetId);
        return repository.findById(widgetId);
    }

    @Override
    @Transactional
    public WidgetEntity save(@NotNull WidgetEntity widget) {
        if (widget.getZ() == null) {
            widget.setZ(repository.findMaxZ(widget.getId()) + 1);
        } else {
            shiftByZIndex(widget.getZ(), widget.getId());
        }

        return repository.save(widget);
    }

    private void shiftByZIndex(@NotNull Integer newZ, Long widgetId) {
        Objects.requireNonNull(newZ);
        var sameZWidgetOptional = widgetId != null ?
                repository.findByZAndIdNot(newZ, widgetId) :
                repository.findByZ(newZ);

        List<WidgetEntity> existed = new ArrayList<>();

        while (sameZWidgetOptional.isPresent()) {
            var widget = sameZWidgetOptional.get();
            existed.add(sameZWidgetOptional.get());
            sameZWidgetOptional = repository.findByZ(widget.getZ() + 1);
        }

        if (!existed.isEmpty()) {
            repository.shirtZIndices(existed);
        }
    }

    @Override
    @Transactional
    public void delete(@NotNull Long widgetId) {
        Objects.requireNonNull(widgetId);

        if (repository.existsById(widgetId)) {
            repository.deleteById(widgetId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WidgetEntity> findByFilter(@NotNull WidgetSearchRequestDto filter, @NotNull Pageable pageable) {
        return repository.findByExpression(
                filter.getxMin().doubleValue(),
                filter.getxMax().doubleValue(),
                filter.getyMin().doubleValue(),
                filter.getyMax().doubleValue(),
                pageable
        );
    }
}
