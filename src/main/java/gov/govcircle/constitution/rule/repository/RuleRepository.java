package gov.govcircle.constitution.rule.repository;

import gov.govcircle.constitution.rule.model.entities.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {
}
