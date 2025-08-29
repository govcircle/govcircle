package gov.govcircle.constitution.constitution.repository;

import gov.govcircle.constitution.constitution.model.entities.VotingThreshold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingThresholdRepository extends JpaRepository<VotingThreshold, Long> {
}
