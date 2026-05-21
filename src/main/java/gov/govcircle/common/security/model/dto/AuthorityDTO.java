package gov.govcircle.common.security.model.dto;

import gov.govcircle.common.config.Configs;
import gov.govcircle.common.security.model.entity.AuthorityType;
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
public class AuthorityDTO {

    private Long id;
    private String title;
    private String description;
    private Long code;
    private AuthorityType authorityType;
    private RoleDTO role;

    public static List<RoleAuthorityDTO> spo() {
        return List.of(
                RoleAuthorityDTO.builder()
                        .role(RoleDTO.spoId())
                        .authority(new AuthorityDTO(
                                        Configs.ROLES.AUTHORITY_PROPOSAL_CREATE_ID,
                                        Configs.ROLES.AUTHORITY_PROPOSAL_CREATE_TITLE,
                                        Configs.ROLES.AUTHORITY_PROPOSAL_CREATE_DESCRIPTION,
                                        Configs.ROLES.AUTHORITY_PROPOSAL_CREATE_CODE,
                                        AuthorityType.CREATE,
                                        RoleDTO.spoId()
                                )
                        )
                        .build()

        );

    }

    public static List<RoleAuthorityDTO> cc() {
        return List.of(
                RoleAuthorityDTO.builder()
                        .role(RoleDTO.ccId())
                        .authority(new AuthorityDTO(
                                        Configs.ROLES.AUTHORITY_PROPOSAL_CREATE_ID,
                                        Configs.ROLES.AUTHORITY_PROPOSAL_CREATE_TITLE,
                                        Configs.ROLES.AUTHORITY_PROPOSAL_CREATE_DESCRIPTION,
                                        Configs.ROLES.AUTHORITY_PROPOSAL_CREATE_CODE,
                                        AuthorityType.CREATE,
                                        RoleDTO.ccId()
                                )
                        )
                        .build()

        );

    }

    public static List<RoleAuthorityDTO> dRep() {
        return List.of(
                RoleAuthorityDTO.builder()
                        .role(RoleDTO.dRepId())
                        .authority(
                                new AuthorityDTO(
                                        Configs.ROLES.AUTHORITY_PROPOSAL_CREATE_ID,
                                        Configs.ROLES.AUTHORITY_PROPOSAL_CREATE_TITLE,
                                        Configs.ROLES.AUTHORITY_PROPOSAL_CREATE_DESCRIPTION,
                                        Configs.ROLES.AUTHORITY_PROPOSAL_CREATE_CODE,
                                        AuthorityType.CREATE,
                                        RoleDTO.dRepId()
                                )
                        )
                        .build()

        );

    }

    public static List<RoleAuthorityDTO> user() {
        return List.of();

    }

}
