package org.widget.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.widget.entity.WidgetEntity;
import org.widget.internal.models.WidgetDto;

import javax.validation.constraints.NotNull;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        componentModel = "spring"
)
public interface WidgetMapper {

    WidgetDto toDto(@NotNull WidgetEntity widget);
}
