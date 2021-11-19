package org.widget.repository;

import org.widget.entity.WidgetEntity;
import org.widget.internal.models.WidgetSearchRequestDto;

import java.util.Objects;

public class WidgetMemoryServiceUtil {

    /**
     * Check widget's width and height and match it with filter window
     *
     * @return true if widget in window
     */
    public static boolean widgetInWindow(WidgetEntity widget, WidgetSearchRequestDto filter) {
        Objects.requireNonNull(widget);
        Objects.requireNonNull(filter);

        boolean xExpression = checkLength(filter.getxMin(), filter.getxMax(), widget.getX(), widget.getWidth());
        boolean yExpression = checkLength(filter.getyMin(), filter.getyMax(), widget.getY(), widget.getHeight());

        return xExpression && yExpression;
    }

    /**
     * Check widget's size
     *
     * @param min    - min X or Y
     * @param max    - max X or Y
     * @param center - widget's center
     * @param length - widget's width or height
     * @return true if widget's width or height less or equal filter window height or width
     */
    private static boolean checkLength(Integer min, Integer max, Integer center, Integer length) {
        Objects.requireNonNull(min);
        Objects.requireNonNull(max);
        Objects.requireNonNull(center);
        Objects.requireNonNull(length);

        double halfLength = length.doubleValue() / 2.0;
        return min.doubleValue() <= (center.doubleValue() - halfLength) &&
                max.doubleValue() >= (center.doubleValue() + halfLength);
    }
}
