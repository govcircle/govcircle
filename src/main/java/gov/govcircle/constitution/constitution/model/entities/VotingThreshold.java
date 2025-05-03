package gov.govcircle.constitution.constitution.model.entities;

import gov.govcircle.base.model.entity.BaseEntity;
import gov.govcircle.comon.models.entity.Actor;
import gov.govcircle.constitution.constitution.model.enums.ActionType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class VotingThreshold extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    @ManyToOne
    private Actor actor;

    private int percentage;

}
