package gov.govcircle.constitution.constitution.repository;

import gov.govcircle.constitution.constitution.model.entities.ConstitutionBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConstitutionBranchRepository extends JpaRepository<ConstitutionBranch, Long> {
}
