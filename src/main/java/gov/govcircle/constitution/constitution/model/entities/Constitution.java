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
public class Constitution extends BaseEntity {


    @ManyToOne
    private Action govAction;

    private String dataHash;
    private String script;
    private String url;
}
