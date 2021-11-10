package org.widget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.widget.entity.WidgetEntity;

public interface WidgetRepository extends JpaRepository<WidgetEntity, Long> {

    boolean existsByZ(Integer z);
    boolean existsByZAndIdNot(Integer z, Long id);
}
