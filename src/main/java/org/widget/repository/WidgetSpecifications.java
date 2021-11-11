package org.widget.repository;

import org.springframework.data.jpa.domain.Specification;
import org.widget.entity.WidgetEntity;
import org.widget.internal.models.WidgetSearchRequestDto;

public class WidgetSpecifications {

    public static Specification<WidgetEntity> withWindowSize(WidgetSearchRequestDto filter) {
        return (root, query, cb) -> cb.and(
                cb.lessThanOrEqualTo(root.get("width"), filter.getxMax() - filter.getxMin()),
                cb.lessThanOrEqualTo(root.get("height"), filter.getyMax() - filter.getyMin())
        );
    }

    public static Specification<WidgetEntity> withCenterInWindow(WidgetSearchRequestDto filter) {
        return (root, query, cb) -> cb.and(
                cb.between(root.get("x"), filter.getxMin(), filter.getxMax()),
                cb.between(root.get("y"), filter.getyMin(), filter.getyMax())
        );
    }
}
