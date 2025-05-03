package gov.govcircle.comon.models.entity;

import gov.govcircle.base.model.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ApplicationUser extends BaseEntity {

    private String wallet;
    private String email;
    private String username;
    private String adaHandle;
    private String password;
    private String role;

}
