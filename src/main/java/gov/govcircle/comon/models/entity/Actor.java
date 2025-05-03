package gov.govcircle.comon.models.entity;


import gov.govcircle.base.model.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Entity(name = "actor")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Actor extends BaseEntity {

    private String title;
}
