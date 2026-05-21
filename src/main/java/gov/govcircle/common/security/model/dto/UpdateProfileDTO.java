package gov.govcircle.common.security.model.dto;

import gov.govcircle.common.security.model.vo.DataSignatureVO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UpdateProfileDTO {

    private String id;
    private String keyIdentifier;
    private String username;
    private String email;
}
