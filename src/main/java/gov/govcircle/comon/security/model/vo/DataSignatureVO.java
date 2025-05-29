package gov.govcircle.comon.security.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true, fluent = true)
public class DataSignatureVO {

    @JsonProperty(value = "key")
    private String key;

    @JsonProperty(value = "signature")
    private String signature;
}
