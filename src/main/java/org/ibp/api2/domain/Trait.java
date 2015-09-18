package org.ibp.api2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A Trait.
 */
@Entity
@Table(name = "TRAIT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="trait")
public class Trait implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "property")
    private String property;
    
    @Column(name = "measurement_method")
    private String measurementMethod;
    
    @Column(name = "scale")
    private String scale;

    @ManyToMany(mappedBy = "traitss")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Study> studiess = new HashSet<>();

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

    public Set<Study> getStudiess() {
        return studiess;
    }

    public void setStudiess(Set<Study> studys) {
        this.studiess = studys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Trait trait = (Trait) o;

        if ( ! Objects.equals(id, trait.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Trait{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", description='" + description + "'" +
                ", property='" + property + "'" +
                ", measurementMethod='" + measurementMethod + "'" +
                ", scale='" + scale + "'" +
                '}';
    }
}
