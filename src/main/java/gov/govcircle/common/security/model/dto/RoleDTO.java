package gov.govcircle.common.security.model.dto;

import gov.govcircle.common.config.Configs;
import gov.govcircle.common.security.model.entity.CardanoActorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {

    private Long id;
    private String title;
    private String description;
    private Long code;
    private List<RoleAuthorityDTO> authorities;

    public static RoleDTO spo() {
        return new RoleDTO(
                Configs.ROLES.SPO_ROLE_ID,
                Configs.ROLES.SPO_ROLE_TITLE,
                Configs.ROLES.SPO_ROLE_DESCRIPTION,
                Configs.ROLES.SPO_ROLE_CODE,
                AuthorityDTO.spo()
        );

    }
    public static RoleDTO spoId() {
        return RoleDTO.builder()
                .id(Configs.ROLES.SPO_ROLE_ID)
                .build();

    }

    public static RoleDTO cc() {
        return new RoleDTO(
                Configs.ROLES.CC_ROLE_ID,
                Configs.ROLES.CC_ROLE_TITLE,
                Configs.ROLES.CC_ROLE_DESCRIPTION,
                Configs.ROLES.CC_ROLE_CODE,
                AuthorityDTO.cc()
        );

    }
    public static RoleDTO ccId() {
        return RoleDTO.builder()
                .id(Configs.ROLES.CC_ROLE_ID)
                .build();

    }

    public static RoleDTO dRep() {
        return new RoleDTO(
                Configs.ROLES.DREP_ROLE_ID,
                Configs.ROLES.DREP_ROLE_TITLE,
                Configs.ROLES.DREP_ROLE_DESCRIPTION,
                Configs.ROLES.DREP_ROLE_CODE,
                AuthorityDTO.dRep()
        );

    }
    public static RoleDTO dRepId() {
        return RoleDTO.builder()
                .id(Configs.ROLES.DREP_ROLE_ID)
                .build();

    }

    public static RoleDTO user() {
        return new RoleDTO(
                Configs.ROLES.WALLET_ROLE_ID,
                Configs.ROLES.WALLET_ROLE_TITLE,
                Configs.ROLES.WALLET_ROLE_DESCRIPTION,
                Configs.ROLES.WALLET_ROLE_CODE,
                AuthorityDTO.user()
        );

    }

    public static RoleDTO userId() {
        return RoleDTO.builder()
                .id(Configs.ROLES.WALLET_ROLE_ID)
                .build();

    }

    public static RoleDTO getCardanoRole(CardanoActorType cardanoActorType) {
        return switch (cardanoActorType) {
            case WALLET -> user();
            case DREP -> dRep();
            case SPO -> spo();
            case CC -> cc();
        };

    }


}
