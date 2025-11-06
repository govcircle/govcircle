package gov.govcircle.common.security.model.dto;

import gov.govcircle.common.config.Configs;
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
                Configs.SPO_ROLE_ID,
                Configs.SPO_ROLE_TITLE,
                Configs.SPO_ROLE_DESCRIPTION,
                Configs.SPO_ROLE_CODE,
                AuthorityDTO.spo()
        );

    }
    public static RoleDTO spoId() {
        return RoleDTO.builder()
                .id(Configs.SPO_ROLE_ID)
                .build();

    }

    public static RoleDTO cc() {
        return new RoleDTO(
                Configs.CC_ROLE_ID,
                Configs.CC_ROLE_TITLE,
                Configs.CC_ROLE_DESCRIPTION,
                Configs.CC_ROLE_CODE,
                AuthorityDTO.cc()
        );

    }
    public static RoleDTO ccId() {
        return RoleDTO.builder()
                .id(Configs.CC_ROLE_ID)
                .build();

    }

    public static RoleDTO dRep() {
        return new RoleDTO(
                Configs.DREP_ROLE_ID,
                Configs.DREP_ROLE_TITLE,
                Configs.DREP_ROLE_DESCRIPTION,
                Configs.DREP_ROLE_CODE,
                AuthorityDTO.dRep()
        );

    }
    public static RoleDTO dRepId() {
        return RoleDTO.builder()
                .id(Configs.DREP_ROLE_ID)
                .build();

    }

    public static RoleDTO user() {
        return new RoleDTO(
                Configs.USER_ROLE_ID,
                Configs.USER_ROLE_TITLE,
                Configs.USER_ROLE_DESCRIPTION,
                Configs.USER_ROLE_CODE,
                AuthorityDTO.user()
        );

    }
    public static RoleDTO userId() {
        return RoleDTO.builder()
                .id(Configs.USER_ROLE_ID)
                .build();

    }


}
