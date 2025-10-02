package gov.govcircle.constitution.constitution.model.entities;

import gov.govcircle.core.entities.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Entity
@Table(name = "gc_merge_request")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MergeRequest extends BaseEntity {
}
