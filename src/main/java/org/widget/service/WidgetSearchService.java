package org.widget.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.widget.entity.WidgetEntity;
import org.widget.internal.models.WidgetSearchRequestDto;
import org.widget.repository.WidgetRepository;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class WidgetSearchService {

    private final WidgetRepository repository;

    /**
     * Search widgets in area (xMin;yMin) - (xMax;yMax)
     * @param filter - xMin, xMax, yMin, yMax, page and size of page
     * @return Page with entities in area
     */
    public Page<WidgetEntity> search(@NotNull WidgetSearchRequestDto filter) {
        Objects.requireNonNull(filter);
        Objects.requireNonNull(filter.getPage());
        Objects.requireNonNull(filter.getSize());

        var sort = Sort.by(Sort.Direction.ASC, "z");
        var pageable = PageRequest.of(filter.getPage() - 1, filter.getSize(), sort);

        Objects.requireNonNull(filter.getxMin());
        Objects.requireNonNull(filter.getxMax());
        Objects.requireNonNull(filter.getyMin());
        Objects.requireNonNull(filter.getyMax());

        return repository.findByExpression(
                filter.getxMin().doubleValue(),
                filter.getxMax().doubleValue(),
                filter.getyMin().doubleValue(),
                filter.getyMax().doubleValue(),
                pageable
        );
    }
}
