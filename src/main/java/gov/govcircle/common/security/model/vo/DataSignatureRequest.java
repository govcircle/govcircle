package gov.govcircle.common.security.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class DataSignatureRequest {

    private String key;
    private String signature;
}
