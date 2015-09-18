package org.ibp.api2.repository;

import org.ibp.api2.domain.Trait;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Trait entity.
 */
public interface TraitRepository extends JpaRepository<Trait,Long> {

}
