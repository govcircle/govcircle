package gov.govcircle.constitution.constitution.model.entities;


import gov.govcircle.base.model.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ConstitutionBranch extends BaseEntity {

    @ManyToOne
    private Constitution constitution;


}
