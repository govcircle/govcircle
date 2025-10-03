package gov.govcircle.constitution.constitution.model.entities;

import gov.govcircle.core.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Data
@Entity
@Table(
        name = "gc_constitution_branch",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"gov_action_id"}, name = "opinion_gov_action_unique_key")
        }
)
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ConstitutionBranch extends BaseEntity {


    @OneToOne
    @JoinColumn(
            name = "gov_action_id",
            foreignKey = @ForeignKey(name = "opinion_gov_action_fk_id")
    )
    private Action govAction;

    private String dataHash;
    private String script;
    private String url;

    @Embedded
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> certificate;


}
