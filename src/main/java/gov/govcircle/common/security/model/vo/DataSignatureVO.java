package gov.govcircle.common.security.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DataSignatureVO {

    @JsonProperty(value = "key")
    private String key;

    @JsonProperty(value = "signature")
    private String signature;
}
