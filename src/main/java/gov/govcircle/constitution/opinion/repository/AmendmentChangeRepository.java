package gov.govcircle.constitution.opinion.repository;

import gov.govcircle.constitution.opinion.model.entities.Amendment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmendmentChangeRepository extends JpaRepository<Amendment, Long> {
}
