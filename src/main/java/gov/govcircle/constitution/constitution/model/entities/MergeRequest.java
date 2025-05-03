package gov.govcircle.constitution.constitution.model.entities;

import gov.govcircle.base.model.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MergeRequest extends BaseEntity {
}
