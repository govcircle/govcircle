package gov.govcircle.constitution.constitution.repository;

import gov.govcircle.constitution.constitution.model.entities.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {
}
