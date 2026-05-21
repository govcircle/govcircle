package gov.govcircle.common.security.model.entity;

import gov.govcircle.common.config.Configs;

public enum CardanoActorType {
    WALLET(Configs.ROLES.WALLET_ROLE_TITLE),
    DREP(Configs.ROLES.DREP_ROLE_TITLE),
    SPO(Configs.ROLES.SPO_ROLE_TITLE),
    CC(Configs.ROLES.CC_ROLE_TITLE);
    private final String title;
    CardanoActorType(String title) {
        this.title = title;

    }
    public String title() {
        return this.title;

    }
    public static CardanoActorType fromRegistrationStatus(String title) {
        for (CardanoActorType actorType : CardanoActorType.values()) {
            if (actorType.title().equals(title)) {
                return actorType;

            }

        }
        throw new IllegalArgumentException("Unknown actor type: " + title);

    }

}
