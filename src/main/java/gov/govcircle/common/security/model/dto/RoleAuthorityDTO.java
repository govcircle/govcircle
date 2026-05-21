package gov.govcircle.common.security.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class RoleAuthorityDTO {

    private Long id;
    private RoleDTO role;
    private AuthorityDTO authority;
    private int startSlot;
    private int endSlot;
}
