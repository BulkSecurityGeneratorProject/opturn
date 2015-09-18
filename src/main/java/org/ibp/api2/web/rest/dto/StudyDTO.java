package org.ibp.api2.web.rest.dto;

import org.joda.time.LocalDate;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import org.ibp.api2.domain.enumeration.StudyType;

/**
 * A DTO for the Study entity.
 */
public class StudyDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private String objective;

    private StudyType type;

    private LocalDate startDate;

    private LocalDate endDate;
    private Set<TraitDTO> traitss = new HashSet<>();

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

    public Set<TraitDTO> getTraitss() {
        return traitss;
    }

    public void setTraitss(Set<TraitDTO> traits) {
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

        StudyDTO studyDTO = (StudyDTO) o;

        if ( ! Objects.equals(id, studyDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StudyDTO{" +
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
