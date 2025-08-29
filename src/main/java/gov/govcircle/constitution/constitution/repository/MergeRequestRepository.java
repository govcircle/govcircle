package gov.govcircle.constitution.constitution.repository;

import gov.govcircle.constitution.constitution.model.entities.MergeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MergeRequestRepository extends JpaRepository<MergeRequest, Long> {
}
