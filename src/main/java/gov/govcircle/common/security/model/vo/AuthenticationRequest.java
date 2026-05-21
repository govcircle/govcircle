package gov.govcircle.common.security.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AuthenticationRequest {

    public AuthenticationRequest(
            DataSignatureVO dataSignature
    ) {
        this.dataSignature = dataSignature;

    }
    private String username;
    private String email;
    private String keyIdentifier;

    private String password;

    private String description;
    private DataSignatureVO dataSignature; //TODO: removal candidate
}
