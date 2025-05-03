package gov.govcircle.base.model.entity;

import gov.govcircle.comon.models.entity.ApplicationUser;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    private ApplicationUser createdBy;

    @ManyToOne
    private ApplicationUser updatedBy;

    private String ip;

}
