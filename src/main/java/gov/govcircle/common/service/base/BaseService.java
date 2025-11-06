package gov.govcircle.common.service.base;

import gov.govcircle.core.entities.BaseEntity;

public interface BaseService {

    BaseEntity fillSave(BaseEntity baseEntity);
    BaseEntity fillUpdate(BaseEntity baseEntity);
}
