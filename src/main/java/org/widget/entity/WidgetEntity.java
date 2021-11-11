package org.widget.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.util.Objects;

@Entity
@Setter
@Getter
@Accessors(chain = true)
@Table(name = "widgets")
public class WidgetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, insertable = false, updatable = false)
    Long id;

    @Column(nullable = false)
    Integer x;

    @Column(nullable = false)
    Integer y;

    @Column(nullable = false, unique = true)
    Integer z;

    @Positive
    @Column(nullable = false)
    Integer width;

    @Positive
    @Column(nullable = false)
    Integer height;

    @Version
    @Column(nullable = false)
    Integer version = 1;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WidgetEntity that = (WidgetEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "WidgetEntity{" + id + " (" + x + ";" + y + "), Z: "
                + z + ", Width: " + width + ", Height: " + height + '}';
    }
}
