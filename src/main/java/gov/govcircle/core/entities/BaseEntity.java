package gov.govcircle.core.entities;

import gov.govcircle.common.security.model.entity.ApplicationUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;


import java.time.LocalDateTime;


@Data
@Accessors(chain = true)
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(
            name = "created_by_id",
            foreignKey = @ForeignKey(name = "creating_user_fk_id")
    )
    private ApplicationUser createdBy;

    @ManyToOne
    @JoinColumn(
            name = "updated_by_id",
            foreignKey = @ForeignKey(name = "updating_user_fk_id")
    )
    private ApplicationUser updatedBy;
    private String ip;

}