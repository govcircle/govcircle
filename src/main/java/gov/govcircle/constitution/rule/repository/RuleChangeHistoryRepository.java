package gov.govcircle.constitution.rule.repository;

import gov.govcircle.constitution.rule.model.entities.RuleChangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleChangeHistoryRepository extends JpaRepository<RuleChangeHistory, Long> {
}
