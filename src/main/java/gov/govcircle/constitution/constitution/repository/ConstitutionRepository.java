package gov.govcircle.constitution.constitution.repository;

import gov.govcircle.constitution.constitution.model.entities.Constitution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConstitutionRepository extends JpaRepository<Constitution, Long> {
}
