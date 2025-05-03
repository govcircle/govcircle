package gov.govcircle.comon.models.entity;


import gov.govcircle.base.model.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserAct extends BaseEntity {

    @ManyToOne
    private ApplicationUser applicationUser;

    @ManyToOne
    private Actor actor;

    private int startEpoch;
    private int endEpoch;
}
