package gov.govcircle.constitution.amendment.model.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

@Converter
public class RuleChangeDataJsonConverter implements AttributeConverter<List<RuleChangeData>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<RuleChangeData> ruleChangeDataList) {
        try {
            return objectMapper.writeValueAsString(ruleChangeDataList);

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting RuleChangeData list to JSON string", e);

        }
    }

    @Override
    public List<RuleChangeData> convertToEntityAttribute(String json) {
        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting JSON string to RuleChangeData List", e);

        }
    }
}
