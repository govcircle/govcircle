package gov.govcircle.common.security.model.entity;

import gov.govcircle.core.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Entity
@Table(name = "gc_authority")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Authority extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false, unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    private AuthorityType authorityType;



}
