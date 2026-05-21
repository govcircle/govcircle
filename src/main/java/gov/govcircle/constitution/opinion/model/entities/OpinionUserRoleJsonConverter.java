package gov.govcircle.constitution.opinion.model.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Converter
@Component
@RequiredArgsConstructor
public class OpinionUserRoleJsonConverter implements AttributeConverter<List<OpinionUserRole>, String> {

    private final ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(List<OpinionUserRole> opinionUserRoleList) {
        try {
            return objectMapper.writeValueAsString(opinionUserRoleList);

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting OpinionUserRoleList list to JSON string", e);

        }

    }

    @Override
    public List<OpinionUserRole> convertToEntityAttribute(String json) {
        if (json == null) {
            return null;

        }
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting JSON string to UserRole List", e);

        }

    }

}
