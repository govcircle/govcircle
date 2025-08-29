package gov.govcircle.constitution.rule.repository;

import gov.govcircle.constitution.rule.model.entities.ConstitutionRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConstitutionRuleRepository extends JpaRepository<ConstitutionRule, Long> {
}
