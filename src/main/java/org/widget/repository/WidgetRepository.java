package org.widget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.widget.entity.WidgetEntity;

public interface WidgetRepository extends
        JpaRepository<WidgetEntity, Long>,
        JpaSpecificationExecutor<WidgetEntity> {

    boolean existsByZ(Integer z);

    boolean existsByZAndIdNot(Integer z, Long id);
}
