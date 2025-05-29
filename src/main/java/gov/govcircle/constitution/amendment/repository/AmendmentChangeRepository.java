package gov.govcircle.constitution.amendment.repository;

import gov.govcircle.constitution.amendment.model.entities.AmendmentChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmendmentChangeRepository extends JpaRepository<AmendmentChange, Long> {
}
