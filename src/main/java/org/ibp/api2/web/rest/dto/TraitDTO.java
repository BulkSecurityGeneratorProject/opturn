package org.ibp.api2.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Trait entity.
 */
public class TraitDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private String property;

    private String measurementMethod;

    private String scale;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getMeasurementMethod() {
        return measurementMethod;
    }

    public void setMeasurementMethod(String measurementMethod) {
        this.measurementMethod = measurementMethod;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TraitDTO traitDTO = (TraitDTO) o;

        if ( ! Objects.equals(id, traitDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TraitDTO{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", description='" + description + "'" +
                ", property='" + property + "'" +
                ", measurementMethod='" + measurementMethod + "'" +
                ", scale='" + scale + "'" +
                '}';
    }
}
