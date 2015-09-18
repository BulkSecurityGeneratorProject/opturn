package org.ibp.api2.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.ibp.api2.domain.util.CustomLocalDateSerializer;
import org.ibp.api2.domain.util.ISO8601LocalDateDeserializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import org.ibp.api2.domain.enumeration.StudyType;

/**
 * A Study.
 */
@Entity
@Table(name = "STUDY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="study")
public class Study implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "objective")
    private String objective;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private StudyType type;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "STUDY_TRAITS",
               joinColumns = @JoinColumn(name="studys_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="traitss_id", referencedColumnName="ID"))
    private Set<Trait> traitss = new HashSet<>();

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

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public StudyType getType() {
        return type;
    }

    public void setType(StudyType type) {
        this.type = type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<Trait> getTraitss() {
        return traitss;
    }

    public void setTraitss(Set<Trait> traits) {
        this.traitss = traits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Study study = (Study) o;

        if ( ! Objects.equals(id, study.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Study{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", description='" + description + "'" +
                ", objective='" + objective + "'" +
                ", type='" + type + "'" +
                ", startDate='" + startDate + "'" +
                ", endDate='" + endDate + "'" +
                '}';
    }
}
