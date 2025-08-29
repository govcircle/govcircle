package gov.govcircle.common.indexer.dbsync.model.entity;

import gov.govcircle.common.models.entity.Addr29;
import gov.govcircle.common.models.entity.Hash28;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "stake_address")
public class StakeAddress {

    @Id
    private Long id;
    private String view;

//    @Embedded
//    @AttributeOverride(
//            name = "value",
//            column = @Column(
//                    name = "hash_raw",
//                    columnDefinition = "hash28type"
//            )
//    )
    @Column(name = "hash_raw")
    private byte[] hashRaw;

//    @Embedded
//    @AttributeOverride(
//            name = "value",
//            column = @Column(
//                    name = "script_hash",
//                    columnDefinition = "addr29type"
//            )
//    )
    @Column(name = "script_hash")
    private byte[] scriptHash;

}
