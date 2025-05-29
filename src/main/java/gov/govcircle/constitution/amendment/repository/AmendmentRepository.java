package gov.govcircle.constitution.amendment.repository;

import gov.govcircle.constitution.amendment.model.entities.Amendment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmendmentRepository extends JpaRepository<Amendment, Long> {
}
