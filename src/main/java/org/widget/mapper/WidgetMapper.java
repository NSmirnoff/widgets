package org.widget.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.data.domain.Page;
import org.widget.entity.WidgetEntity;
import org.widget.internal.models.WidgetDto;
import org.widget.internal.models.WidgetSearchResponseDto;

import javax.validation.constraints.NotNull;
import java.util.stream.Collectors;

@Mapper(
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        componentModel = "spring"
)
public interface WidgetMapper {

    WidgetDto toDto(@NotNull WidgetEntity widget);

    default WidgetSearchResponseDto toDto(@NotNull Page<WidgetEntity> page) {
        return new WidgetSearchResponseDto()
                .page(page.getNumber() + 1)
                .pages(page.getTotalPages())
                .size(page.getSize())
                .total(page.getTotalElements())
                .content(page.getContent().stream().map(this::toDto).collect(Collectors.toList()));
    }
}
