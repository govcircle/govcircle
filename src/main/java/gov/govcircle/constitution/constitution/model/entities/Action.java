package gov.govcircle.constitution.constitution.model.entities;

import gov.govcircle.base.model.entity.BaseEntity;
import gov.govcircle.constitution.constitution.model.enums.ActionStatus;
import gov.govcircle.constitution.constitution.model.enums.ActionType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Action extends BaseEntity {

    private String txHash;
    private String certIndex;
    private String title;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;
    private String stakeAddress;
    private String depositAmount;

    private int enactedEpoch;
    private int droppedEpoch;
    private int expiredEpoch;
    private int expiryEpoch; // to date
    private int ratifiedEpoch;
    private int submissionEpoch;

    private int voteYes;
    private int voteNo;
    private int voteAbstain;

    private String tag;

    @Enumerated(EnumType.STRING)
    private ActionStatus status;

}
