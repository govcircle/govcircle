package gov.govcircle.constitution.opinion.model.entities;

import gov.govcircle.common.security.model.entity.ApplicationUser;
import gov.govcircle.constitution.opinion.model.enums.OpinionStatus;
import gov.govcircle.core.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Entity
@Table(
        name = "gc_opinion",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"owner_id"}, name = "opinion_owner_unique_key")
        }
)
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Opinion extends BaseEntity {

    @OneToOne
    @JoinColumn(
            name = "owner_id",
            foreignKey = @ForeignKey(name = "opinion_owner_fk_id"),
            nullable = false
    )
    private ApplicationUser owner;
    private String intention;

    @Convert(converter = OpinionUserRoleJsonConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<OpinionUserRole> userAct;

    @Enumerated(EnumType.STRING)
    private OpinionStatus status;


}
