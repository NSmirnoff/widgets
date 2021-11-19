package org.widget.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.widget.entity.WidgetEntity;

import java.util.Optional;

@Profile("database")
public interface WidgetRepository extends
        JpaRepository<WidgetEntity, Long>,
        JpaSpecificationExecutor<WidgetEntity> {

    @Query("select max(w.z) from WidgetEntity w where :id is null or w.id <> :id")
    Integer findMaxZ(@Param("id") Long id);

    Optional<WidgetEntity> findByZ(Integer z);

    Optional<WidgetEntity> findByZAndIdNot(Integer z, Long id);

    @Query("select w from WidgetEntity w where " +
            ":x0 is null or :x1 is null or :y0 is null or :y1 is null or " +
            "(cast(w.x as double) between :x0 and :x1 and cast(w.y as double) between :y0 and :y1 " +
            "and :x0 <= (w.x - (w.width / 2.0)) and :x1 >= (w.x + (w.width / 2.0)) " +
            "and :y0 <= (w.y - (w.height / 2.0)) and :y1 >= (w.y + (w.height / 2.0)))")
    Page<WidgetEntity> findByExpression(
            @Param("x0") Double x0,
            @Param("x1") Double x1,
            @Param("y0") Double y0,
            @Param("y1") Double y1,
            Pageable pageable
    );
}
