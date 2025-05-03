package gov.govcircle.constitution.opinion.model.entities;

import gov.govcircle.base.model.entity.BaseEntity;
import gov.govcircle.constitution.opinion.model.enums.OpinionChangeType;
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
public class OpinionChange extends BaseEntity {

    @ManyToOne
    private Opinion opinion;

    @Enumerated(EnumType.STRING)
    private OpinionChangeType opinionChangeType;

}
