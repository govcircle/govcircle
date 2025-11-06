package gov.govcircle.common.security.model.mapper.entitydto;

import java.util.List;

public interface BaseEntityDTOMapper<Entity, DTO> {

    DTO toDTO(Entity entity);
    Entity toEntity(DTO dto);
    List<DTO> toDTO(List<Entity> entityList);
    List<Entity> toEntity(List<DTO> dtoList);
}
