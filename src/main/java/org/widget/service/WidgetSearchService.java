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

@Slf4j
@Service
@RequiredArgsConstructor
public class WidgetSearchService {

    private final WidgetRepository repository;

    public Page<WidgetEntity> search(WidgetSearchRequestDto filter) {
        var sort = Sort.by(Sort.Direction.ASC, "z");
        var pageable = PageRequest.of(filter.getPage() - 1, filter.getSize(), sort);

        return repository.findByExpression(
                filter.getxMin().doubleValue(),
                filter.getxMax().doubleValue(),
                filter.getyMin().doubleValue(),
                filter.getyMax().doubleValue(),
                pageable
        );
    }
}
